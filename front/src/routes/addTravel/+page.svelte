<script>
	import BusTab from './tabs/BusTab.svelte';
	import TrainTab from './tabs/TrainTab.svelte';
	import FlightTab from './tabs/FlightTab.svelte';

	let selectedTab = 'bus';
</script>

<div class="flex space-x-4 mb-4">
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

<div class="mx-auto w-full max-w-lg px-4">
	{#if selectedTab === 'bus'}
		<BusTab />
	{:else if selectedTab === 'train'}
		<TrainTab />
	{:else if selectedTab === 'flight'}
		<FlightTab />
	{/if}
</div>
