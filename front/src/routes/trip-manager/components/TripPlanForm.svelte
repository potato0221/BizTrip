<script lang="ts">
	import { createEventDispatcher, onMount } from 'svelte';
	import rq from '$lib/rq/rq.svelte';
	import toastr from 'toastr';
	import { ChevronDown, ChevronRight } from 'lucide-svelte';
	import { loadKakaoSdk } from '$lib/kakao/loadKakao';

	const dispatch = createEventDispatcher();

	let showForm = false;
	let planName = '';
	let startAddress = '';
	let endAddress = '';
	let startSuggestions: any[] = [];
	let endSuggestions: any[] = [];
	let startInput: HTMLInputElement;
	let endInput: HTMLInputElement;

	onMount(async () => {
		try {
			await loadKakaoSdk();
			console.log('✅ Kakao SDK 로드 완료');
		} catch (err) {
			console.error('❌ Kakao SDK 로딩 실패', err);
		}
	});

	function searchPlaces(keyword: string, isStart: boolean) {
		if (!window.kakao?.maps?.services) {
			console.warn('카카오 장소 검색 미로드 상태');
			return;
		}

		const ps = new window.kakao.maps.services.Places();
		ps.keywordSearch(keyword, (result, status) => {
			if (status === window.kakao.maps.services.Status.OK) {
				if (isStart) startSuggestions = result;
				else endSuggestions = result;
			}
		});
	}

	function selectSuggestion(item: any, isStart: boolean) {
		const formatted = `${item.place_name} - ${item.address_name}`;
		if (isStart) {
			startAddress = formatted;
			startSuggestions = [];
		} else {
			endAddress = formatted;
			endSuggestions = [];
		}
	}

	async function submitPlan() {
		if (!planName || !startAddress || !endAddress) {
			toastr.error('모든 필드를 입력해주세요.');
			return;
		}

		const { data, error } = await rq.apiEndPoints().POST('/api/v1/trip/addTripPlan', {
			body: { planName, startAddress, endAddress }
		});

		if (error) {
			toastr.error(error.msg);
			return;
		}

		toastr.success(data.msg);
		dispatch('planCreated');
		planName = startAddress = endAddress = '';
	}
</script>

<div class="space-y-4">
	<h2
		class="text-lg font-semibold flex items-center gap-2 cursor-pointer select-none"
		on:click={() => (showForm = !showForm)}
	>
		{#if showForm}
			<ChevronDown size={20} />
		{:else}
			<ChevronRight size={20} />
		{/if}
		여행 플랜 생성
	</h2>

	{#if showForm}
		<div class="space-y-4">
			<div>
				<label class="block mb-1 text-lg font-bold">플랜 이름</label>
				<input
					class="input input-bordered w-full focus:border-blue-900 focus:border-2 focus:outline-none"
					type="text"
					placeholder="플랜 이름"
					bind:value={planName}
				/>
			</div>

			<!-- 출발지 -->
			<div>
				<label class="block mb-1 text-lg font-bold">출발지</label>
				<input
					bind:this={startInput}
					class="input input-bordered w-full focus:border-blue-900 focus:border-2 focus:outline-none"
					type="text"
					placeholder="출발지 (예: 우리집)"
					value={startAddress}
					on:input={(e) => searchPlaces(e.target.value, true)}
				/>
				<ul class="border bg-white rounded shadow text-sm max-h-60 overflow-y-auto divide-y">
					{#each startSuggestions as item}
						<li
							class="px-3 py-2 hover:bg-blue-100 cursor-pointer leading-tight"
							on:click={() => selectSuggestion(item, true)}
						>
							<div class="font-semibold">{item.place_name}</div>
							<div class="text-gray-600 text-sm">{item.address_name}</div>
						</li>
					{/each}
				</ul>
			</div>

			<!-- 도착지 -->
			<div>
				<label class="block mb-1 text-lg font-bold">도착지</label>
				<input
					bind:this={endInput}
					class="input input-bordered w-full focus:border-blue-900 focus:border-2 focus:outline-none"
					type="text"
					placeholder="도착지 (예: 숙소)"
					value={endAddress}
					on:input={(e) => searchPlaces(e.target.value, false)}
				/>
				<ul class="border bg-white rounded shadow text-sm max-h-60 overflow-y-auto divide-y">
					{#each endSuggestions as item}
						<li
							class="px-3 py-2 hover:bg-blue-100 cursor-pointer leading-tight"
							on:click={() => selectSuggestion(item, false)}
						>
							<div class="font-semibold">{item.place_name}</div>
							<div class="text-gray-600 text-sm">{item.address_name}</div>
						</li>
					{/each}
				</ul>
			</div>

			<!-- 생성 버튼 -->
			<button
				class="px-4 py-[0.55rem] text-sm font-bold text-blue-900 border-2 border-blue-900 rounded-lg bg-transparent hover:bg-blue-900 hover:text-white focus:bg-blue-900 focus:text-white transition-all w-full"
				on:click={submitPlan}
			>
				플랜 생성
			</button>
		</div>
	{/if}
</div>
