<script lang="ts">
	import { onMount } from 'svelte';
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';

	export let selectedTransportType: 'bus' | 'train' | 'flight' | null;
	export let selectedTransportId: number | null;
	export let selectedTransportObject: any;

	let selectedTab: 'bus' | 'train' | 'flight' = 'bus';
	let busList: components['schemas']['BusDto'][] = [];
	let trainList: components['schemas']['TrainDto'][] = [];
	let flightList: components['schemas']['FlightDto'][] = [];

	onMount(async () => {
		await loadLists();
	});

	function removeId(name: string): string {
		return name.replace(/\s?\(.*?\)/g, '');
	}

	async function loadLists() {
		const [bus, train, flight] = await Promise.all([
			rq.apiEndPoints().GET('/api/v1/transport/bus/myList'),
			rq.apiEndPoints().GET('/api/v1/transport/train/myList'),
			rq.apiEndPoints().GET('/api/v1/transport/flight/myList')
		]);

		busList = bus.data?.data ?? [];
		trainList = train.data?.data ?? [];
		flightList = flight.data?.data ?? [];
	}

	function formatDateTime(datetime?: string): string {
		if (!datetime) return '';
		const date = new Date(datetime);
		const yyyy = date.getFullYear();
		const MM = String(date.getMonth() + 1).padStart(2, '0');
		const dd = String(date.getDate()).padStart(2, '0');
		const hh = String(date.getHours()).padStart(2, '0');
		const mm = String(date.getMinutes()).padStart(2, '0');
		return `${yyyy}년 ${MM}월 ${dd}일 ${hh}시 ${mm}분`;
	}
</script>

<div class="space-y-4">
	<h2 class="text-lg font-semibold">등록된 교통수단 목록</h2>

	<div class="flex space-x-4">
		{#each ['bus', 'train', 'flight'] as tab}
			<button
				on:click={() => (selectedTab = tab)}
				class="text-lg font-semibold border-b-2 pb-1 transition-all"
				class:border-blue-900={selectedTab === tab}
				class:text-blue-900={selectedTab === tab}
				class:text-gray-500={selectedTab !== tab}
				class:border-transparent={selectedTab !== tab}
			>
				{tab === 'bus' ? '버스' : tab === 'train' ? '기차' : '항공편'}
			</button>
		{/each}
	</div>

	{#if selectedTab === 'bus'}
		<ul class="space-y-2">
			{#each busList as bus}
				<li
					class="border rounded p-3 hover:bg-gray-50 cursor-pointer"
					class:bg-blue-100={selectedTransportType === 'bus' && selectedTransportId === bus.id}
					on:click={() => {
						selectedTransportType = 'bus';
						selectedTransportId = bus.id;
						selectedTransportObject = bus;
					}}
				>
					<p class="font-semibold">{removeId(bus.departureName)} → {removeId(bus.arrivalName)}</p>
					<p class="text-sm text-gray-600">출발: {formatDateTime(bus.departureTime)}</p>
					<p class="text-sm text-gray-600">도착: {formatDateTime(bus.arrivalTime)}</p>
				</li>
			{/each}
		</ul>
	{:else if selectedTab === 'train'}
		<ul class="space-y-2">
			{#each trainList as train}
				<li
					class="border rounded p-3 hover:bg-gray-50 cursor-pointer"
					class:bg-blue-100={selectedTransportType === 'train' && selectedTransportId === train.id}
					on:click={() => {
						selectedTransportType = 'train';
						selectedTransportId = train.id;
						selectedTransportObject = train;
					}}
				>
					<p class="font-semibold">{train.departureName} → {train.arrivalName}</p>
					<p class="text-sm text-gray-600">출발: {formatDateTime(train.departureTime)}</p>
					<p class="text-sm text-gray-600">도착: {formatDateTime(train.arrivalTime)}</p>
				</li>
			{/each}
		</ul>
	{:else}
		<ul class="space-y-2">
			{#each flightList as flight}
				<li
					class="border rounded p-3 hover:bg-gray-50 cursor-pointer"
					class:bg-blue-100={selectedTransportType === 'flight' &&
						selectedTransportId === flight.id}
					on:click={() => {
						selectedTransportType = 'flight';
						selectedTransportId = flight.id;
						selectedTransportObject = flight;
					}}
				>
					<p class="font-semibold">{flight.departureName} → {flight.arrivalName}</p>
					<p class="text-sm text-gray-600">출발: {formatDateTime(flight.departureTime)}</p>
					<p class="text-sm text-gray-600">도착: {formatDateTime(flight.arrivalTime)}</p>
				</li>
			{/each}
		</ul>
	{/if}
</div>
