<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import toastr from 'toastr';
	import { onMount } from 'svelte';
	import { fly } from 'svelte/transition';
	import type { components } from '$lib/types/api/v1/schema';

	let selectedTab = 'plan';
	const tabs = [
		{ id: 'plan', label: '플랜' },
		{ id: 'leg', label: '교통편' }
	];

	let nickname = rq.member.nickname;
	let newNickname = '';
	let showForm = false;

	let tripPlans: components['schemas']['TripPlanDto'][] = [];
	let selectedPlanId: number | null = null;
	let selectedLegId: number | null = null;

	function getPlaceName(name: string, type: string): string {
		name = name.replace(/\([^)]*\)/g, '');
		const suffix = type === 'FLIGHT' ? '공항' : type === 'TRAIN' ? '역' : '고속버스터미널';
		return name.trim() + suffix;
	}

	function getPlaceTrim(name: string, type: string): string {
		name = name.replace(/\([^)]*\)/g, '');
		return name.trim();
	}

	function getIconClass(type: string): string {
		return type === 'FLIGHT'
			? 'fa-solid fa-plane-departure'
			: type === 'TRAIN'
				? 'fa-solid fa-train'
				: 'fa-solid fa-bus';
	}

	function beforeDash(text: string): string {
		return text.split(' - ')[0].trim();
	}

	function formatDateTime(dt: string | Date): string {
		const d = new Date(dt);
		return `${d.getFullYear()}년 ${d.getMonth() + 1}월 ${d.getDate()}일 ${d.getHours()}시 ${String(
			d.getMinutes()
		).padStart(2, '0')}분`;
	}

	function togglePlan(planId: number) {
		selectedPlanId = selectedPlanId === planId ? null : planId;
	}

	function toggleLeg(legId: number) {
		selectedLegId = selectedLegId === legId ? null : legId;
	}

	function openNicknameForm() {
		newNickname = nickname;
		showForm = true;
	}

	function cancelNicknameEdit() {
		showForm = false;
		newNickname = '';
	}

	async function updateNickname() {
		if (newNickname.trim() === '') return toastr.warning('닉네임을 입력하세요');

		const { data, error } = await rq.apiEndPoints().PUT('/api/v1/members/modifyNickName', {
			body: { nickname: newNickname }
		});

		if (error) {
			toastr.error(error.msg);
		} else {
			nickname = data.data.nickname;
			toastr.success(`닉네임이 ${nickname}(으)로 변경되었습니다`);
			showForm = false;
			newNickname = '';
		}
	}

	onMount(async () => {
		const { data, error } = await rq.apiEndPoints().GET('/api/v1/trip/getPlan');
		if (!error) {
			tripPlans = data?.data ?? [];

			tripPlans.forEach((plan) => {
				const leg = plan.legs?.[0];
				console.log('플랜:', plan.planName);
				console.log('출발지:', leg?.departureName);
				console.log('도착지:', leg?.arrivalName);
				console.log('타입:', leg?.transportType);
			});
		} else {
			toastr.error(error.msg);
		}
	});

	async function deleteLeg(legId: number) {
		if (!confirm('정말 이 교통편을 삭제하시겠습니까?')) return;

		const { error } = await rq.apiEndPoints().DELETE('/api/v1/trip/delete/leg/{legId}', {
			params: { path: { legId } }
		});

		if (!error) {
			toastr.success('교통편이 삭제되었습니다');
			tripPlans.forEach((plan) => {
				plan.legs = plan.legs.filter((leg) => leg.id !== legId);
			});
			selectedLegId = null;
		} else {
			toastr.error('삭제 실패');
		}
	}

	async function deletePlan(planId: number) {
		if (!confirm('정말 이 플랜을 삭제하시겠습니까?')) return;

		const { error } = await rq.apiEndPoints().DELETE('/api/v1/trip/delete/plan/{planId}', {
			params: { path: { planId } }
		});
		if (!error) {
			toastr.success('플랜이 삭제되었습니다');
			tripPlans = tripPlans.filter((plan) => plan.id !== planId);
			selectedPlanId = null;
		} else {
			toastr.error('삭제 실패');
		}
	}

	function formatDateOnly(dt: string | Date): string {
		const d = new Date(dt);
		return `${d.getFullYear()}년 ${d.getMonth() + 1}월 ${d.getDate()}일`;
	}
</script>

<div class="text-xl font-bold mb-4">
	<div class="flex items-center justify-between">
		<div class="flex items-center">
			<i class="fa-solid fa-user-gear text-blue-900 mr-1"></i>
			{nickname}
		</div>
		<button
			on:click={openNicknameForm}
			class="px-2 py-2 text-sm font-bold text-blue-900 border-2 border-blue-900 rounded-lg bg-transparent hover:bg-blue-900 hover:text-white transition-all"
		>
			닉네임 변경
		</button>
	</div>

	{#if showForm}
		<div class="mt-3 flex flex-wrap items-center gap-2">
			<input
				bind:value={newNickname}
				type="text"
				placeholder="새 닉네임 입력"
				class="input input-bordered w-40 sm:w-48 focus:border-blue-500 focus:outline-none"
			/>
			<button
				on:click={updateNickname}
				class="px-3 py-2 text-sm font-bold text-white bg-blue-900 rounded-lg hover:bg-blue-800"
				>저장</button
			>
			<button
				on:click={cancelNicknameEdit}
				class="px-3 py-2 text-sm font-bold text-gray-700 border border-gray-400 rounded-lg hover:bg-gray-100"
				>취소</button
			>
		</div>
	{/if}
</div>

<div class="inline-flex space-x-4 mb-4">
	{#each tabs as { id, label }}
		<button
			on:click={() => (selectedTab = id)}
			class="text-lg font-semibold pb-2 transition-all border-b-2"
			class:border-blue-900={selectedTab === id}
			class:text-blue-900={selectedTab === id}
			class:text-gray-500={selectedTab !== id}
			class:border-transparent={selectedTab !== id}
		>
			{label}
		</button>
	{/each}
</div>

{#if selectedTab === 'plan'}
	<h2 class="text-lg font-semibold mb-2">
		<i class="fa-solid fa-clipboard mx-1 text-blue-900"></i> 내 플랜
	</h2>
	{#if tripPlans.length > 0}
		<ul class="space-y-2 mb-6">
			{#each tripPlans as plan (plan.id)}
				{@const leg = plan.legs?.[0]}

				<li
					class={`p-3 rounded cursor-pointer transition-colors duration-300 ${
						selectedPlanId === plan.id
							? 'border-2 border-blue-900 bg-blue-50'
							: 'border border-gray-300'
					}`}
					on:click={() => togglePlan(plan.id)}
				>
					<!-- 제목 + 날짜 -->
					<div class="font-semibold flex items-center justify-between">
						<div class="flex items-center">
							<i class="fa-solid fa-location-dot mr-1 text-blue-900"></i>
							{plan.planName}
						</div>
						{#if leg?.departureTime}
							<span class="text-sm text-gray-500 ml-2">
								{formatDateOnly(leg.departureTime)}
							</span>
						{/if}
					</div>

					<!-- 출발지 → 도착지 -->
					<div class="text-sm text-gray-600 mt-1">
						{beforeDash(plan.startAddress)} →
						{#if leg}
							{getPlaceName(leg.departureName, leg.transportType)}<br />
							{getPlaceName(leg.arrivalName, leg.transportType)} →
						{/if}
						{beforeDash(plan.endAddress)}
					</div>

					<!-- 삭제 버튼 -->
					{#if selectedPlanId === plan.id}
						<div in:fly={{ y: 10, duration: 200 }}>
							<div class="flex justify-end">
								<button
									on:click|stopPropagation={() => deletePlan(plan.id)}
									class="px-3 py-2 text-sm font-bold bg-white text-red-600 border-2 border-red-600 rounded-lg hover:bg-red-600 hover:text-white focus:bg-red-600 focus:text-white transition-all"
								>
									삭제
								</button>
							</div>
						</div>
					{/if}
				</li>
			{/each}
		</ul>
	{:else}
		<p class="text-gray-500">등록된 플랜이 없습니다.</p>
	{/if}
{/if}

{#if selectedTab === 'leg'}
	<h2 class="text-lg font-semibold mb-2">
		<i class="fa-solid fa-rocket mx-1 text-blue-900"></i> 내 교통편
	</h2>
	{#if tripPlans.flatMap((p) => p.legs).length > 0}
		<ul class="space-y-2 mb-6">
			{#each tripPlans.flatMap((p) => p.legs) as leg (leg.id)}
				<li
					on:click={() => toggleLeg(leg.id)}
					class={`p-3 rounded cursor-pointer transition-colors duration-300 ${
						selectedLegId === leg.id
							? 'border-2 border-blue-900 bg-blue-50'
							: 'border border-gray-300'
					}`}
				>
					<div class="font-semibold text-base mb-1">
						<i class={getIconClass(leg.transportType) + ' mr-1 text-blue-900'}></i>{getPlaceTrim(
							leg.departureName,
							leg.transportType
						)} →
						<i class={getIconClass(leg.transportType) + ' mr-1 text-blue-900'}></i>{getPlaceTrim(
							leg.arrivalName,
							leg.transportType
						)}
					</div>
					<div class="text-sm text-gray-700">
						출발: {formatDateTime(leg.departureTime)}<br />
						도착: {formatDateTime(leg.arrivalTime)}
					</div>
					{#if selectedLegId === leg.id}
						<div in:fly={{ y: 10, duration: 200 }} class="flex justify-end mt-2">
							<button
								on:click|stopPropagation={() => deleteLeg(leg.id)}
								class="px-3 py-1 text-sm font-bold bg-white text-red-600 border-2 border-red-600 rounded hover:bg-red-600 hover:text-white transition-all"
							>
								삭제
							</button>
						</div>
					{/if}
				</li>
			{/each}
		</ul>
	{:else}
		<p class="text-gray-500">등록된 교통편이 없습니다.</p>
	{/if}
{/if}
