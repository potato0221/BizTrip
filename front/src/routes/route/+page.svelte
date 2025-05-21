<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';
	import { onMount } from 'svelte';

	let tripPlans: components['schemas']['TripPlanDto'][] = [];
	let selectedPlan: components['schemas']['TripPlanDto'] | null = null;
	let routeUrlStart = '';
	let routeUrlEnd = '';

	onMount(async () => {
		const { data, error } = await rq.apiEndPoints().GET('/api/v1/trip/getPlan');
		if (!error) {
			tripPlans = data?.data ?? [];
		} else {
			rq.msgError(error.msg);
		}
	});

	function getPlaceName(name: string, type: string): string {
		name = name.replace(/\([^)]*\)/g, '');
		const suffix = type === 'FLIGHT' ? '공항' : type === 'TRAIN' ? '역' : '고속버스터미널';
		return name.trim() + suffix;
	}

	function handlePlanSelect(plan: components['schemas']['TripPlanDto']) {
		selectedPlan = plan;

		if (plan.legs.length > 0) {
			const leg = plan.legs[0];

			// 출발지 -> 교통수단 출발지
			const fromStart = plan.startAddress.split(' - ')[0];
			const toStart = getPlaceName(leg.departureName, leg.transportType);
			routeUrlStart = `https://map.kakao.com/?sName=${encodeURIComponent(fromStart)}&eName=${encodeURIComponent(toStart)}`;

			// 교통수단 도착지 -> 도착지
			const fromEnd = getPlaceName(leg.arrivalName, leg.transportType);
			const toEnd = plan.endAddress.split(' - ')[0];
			routeUrlEnd = `https://map.kakao.com/?sName=${encodeURIComponent(fromEnd)}&eName=${encodeURIComponent(toEnd)}`;
		}
	}
</script>

<h1 class="text-xl font-bold mb-4"><i class="fa-solid fa-compass text-blue-900"></i> 경로 보기</h1>

{#if tripPlans.length > 0}
	<ul class="space-y-2 mb-6">
		{#each tripPlans as plan}
			<li
				class="border p-3 rounded hover:bg-blue-50 cursor-pointer"
				on:click={() => handlePlanSelect(plan)}
			>
				<div class="font-semibold">{plan.planName}</div>
				<div class="text-sm text-gray-500">
					{plan.startAddress.split(' - ')[0]} → {getPlaceName(
						plan.legs[0]?.departureName ?? '',
						plan.legs[0]?.transportType ?? ''
					)}<br />
					{getPlaceName(plan.legs[0]?.arrivalName ?? '', plan.legs[0]?.transportType ?? '')} → {plan.endAddress.split(
						' - '
					)[0]}
				</div>
			</li>
		{/each}
	</ul>
{:else}
	<p class="text-gray-500">등록된 플랜이 없습니다.</p>
{/if}

{#if routeUrlStart}
	<div class="mt-6">
		<h2 class="text-lg font-semibold">
			<i class="fa-solid fa-map text-blue-900 ml-1"></i> 출발지 → 교통편 위치
		</h2>
		<iframe
			src={routeUrlStart}
			width="100%"
			height="400"
			class="border rounded mt-2"
			allowfullscreen
		></iframe>
		<div class="text-xs text-gray-500 mt-1">
			<i class="fa-solid fa-triangle-exclamation ml-1" style="color: #FFD43B;"></i> 자세한 경로는 카카오맵
			앱에서 확인됩니다.
		</div>
	</div>
{/if}

{#if routeUrlEnd}
	<div class="mt-6">
		<h2 class="text-lg font-semibold">
			<i class="fa-solid fa-map text-blue-900 ml-1"></i> 교통편 → 도착지
		</h2>
		<iframe src={routeUrlEnd} width="100%" height="400" class="border rounded mt-2" allowfullscreen
		></iframe>
		<div class="text-xs text-gray-500 mt-1">
			<i class="fa-solid fa-triangle-exclamation ml-1" style="color: #FFD43B;"></i> 자세한 경로는 카카오맵
			앱에서 확인됩니다.
		</div>
	</div>
{/if}
