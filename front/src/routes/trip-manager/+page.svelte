<script lang="ts">
	import TripPlanForm from './components/TripPlanForm.svelte';
	import TripPlanList from './components/TripPlanList.svelte';
	import TransportList from './components/TransportList.svelte';
	import rq from '$lib/rq/rq.svelte';
	import toastr from 'toastr';

	let selectedTripPlanId: number | null = null;
	let selectedTransportType: 'bus' | 'train' | 'flight' | null = null;
	let selectedTransportId: number | null = null;
	let selectedTransportObject: any = null;

	async function submitLeg() {
		if (
			!selectedTripPlanId ||
			!selectedTransportType ||
			!selectedTransportId ||
			!selectedTransportObject
		)
			return;

		const { departureName, arrivalName, departureTime, arrivalTime } = selectedTransportObject;

		const client = rq.apiEndPointsWithFetch(fetch);

		const { data, error } = await client.POST(`/api/v1/trip/${selectedTripPlanId}/addLeg` as any, {
			body: {
				transportType: selectedTransportType?.toUpperCase(),
				transportId: selectedTransportId,
				departureName,
				arrivalName,
				departureTime,
				arrivalTime
			}
		});

		if (error) {
			toastr.error(error.msg);
			return;
		}
		toastr.success(data.msg);

		selectedTransportId = null;
		selectedTransportType = null;
		selectedTransportObject = null;
	}
</script>

<div class="mx-2 mt-2 space-y-6">
	<h1 class="text-2xl font-bold">플랜 등록</h1>

	<TripPlanForm />

	<TripPlanList
		selectedPlanId={selectedTripPlanId}
		onSelectedPlanChange={(id) => (selectedTripPlanId = id)}
	/>

	<TransportList bind:selectedTransportType bind:selectedTransportId bind:selectedTransportObject />

	{#if selectedTripPlanId !== null && selectedTransportType !== null && selectedTransportId !== null && selectedTransportObject !== null}
		<button
			on:click={submitLeg}
			class="px-4 py-[0.55rem] text-sm font-bold text-blue-900 border-2 border-blue-900 rounded-lg bg-transparent hover:bg-blue-900 hover:text-white focus:bg-blue-900 focus:text-white transition-all w-full"
		>
			플랜에 교통수단 추가하기
		</button>
	{/if}
</div>
