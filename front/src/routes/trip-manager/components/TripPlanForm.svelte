<script lang="ts">
	import { createEventDispatcher } from 'svelte';
	import rq from '$lib/rq/rq.svelte';
	import toastr from 'toastr';
	import { ChevronDown, ChevronRight } from 'lucide-svelte';

	const dispatch = createEventDispatcher();

	let showForm = false;
	let planName = '';
	let startAddress = '';
	let endAddress = '';

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
	<!-- 제목 + 토글 버튼 -->
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

	<!-- 접히는 부분 -->
	{#if showForm}
		<div class="space-y-4">
			<!-- 플랜 이름 -->
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
					class="input input-bordered w-full focus:border-blue-900 focus:border-2 focus:outline-none"
					type="text"
					placeholder="출발지 (예: 우리집)"
					bind:value={startAddress}
				/>
			</div>

			<!-- 도착지 -->
			<div>
				<label class="block mb-1 text-lg font-bold">도착지</label>
				<input
					class="input input-bordered w-full focus:border-blue-900 focus:border-2 focus:outline-none"
					type="text"
					placeholder="도착지 (예: 숙소)"
					bind:value={endAddress}
				/>
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
