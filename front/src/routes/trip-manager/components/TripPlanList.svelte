<script lang="ts">
	import { onMount } from 'svelte';
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';

	const { selectedPlanId, onSelectedPlanChange } = $props<{
		selectedPlanId: number | null;
		onSelectedPlanChange: (id: number) => void;
	}>();

	let tripPlans = $state<components['schemas']['PlanListDto'][]>([]);

	async function loadTripPlans() {
		const { data, error } = await rq.apiEndPoints().GET('/api/v1/trip/planList');
		if (error) {
			rq.msgError(error.msg);
			return;
		}
		tripPlans.length = 0;
		tripPlans.push(...(data?.data ?? []));
	}

	onMount(() => {
		loadTripPlans();
	});
</script>

<div class="space-y-4">
	<h2 class="text-lg font-semibold">내 여행 플랜</h2>

	{#if tripPlans.length > 0}
		<ul class="space-y-2">
			{#each tripPlans as plan}
				<li class="border p-4 rounded shadow-sm cursor-pointer hover:bg-gray-50">
					<label class="flex items-center space-x-2">
						<input
							type="radio"
							name="tripPlan"
							checked={selectedPlanId === plan.planId}
							on:change={() => onSelectedPlanChange(plan.planId)}
						/>
						<span class="font-bold">{plan.planName}</span>
					</label>
				</li>
			{/each}
		</ul>
	{:else}
		<p class="text-gray-500">등록 된 플랜이 없습니다.</p>
	{/if}
</div>
