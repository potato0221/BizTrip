<script>
	import BusTab from './tabs/BusTab.svelte';
	import KtxTab from './tabs/KtxTab.svelte';
	import FlightTab from './tabs/FlightTab.svelte';

	let selectedTab = 'bus';
</script>

<div class="flex space-x-4 mb-4">
	{#each ['bus', 'ktx', 'flight'] as tab}
		<button
			on:click={() => (selectedTab = tab)}
			class="text-lg font-semibold border-b-2 pb-1 transition-all"
			class:border-blue-900={selectedTab === tab}
			class:text-blue-900={selectedTab === tab}
			class:text-gray-500={selectedTab !== tab}
			class:border-transparent={selectedTab !== tab}
		>
			{tab === 'bus' ? '고속버스' : tab === 'ktx' ? 'KTX' : '항공편'}
		</button>
	{/each}
</div>

<div class="mx-auto w-full max-w-lg px-4">
	{#if selectedTab === 'bus'}
		<BusTab />
	{:else if selectedTab === 'ktx'}
		<KtxTab />
	{:else if selectedTab === 'flight'}
		<FlightTab />
	{/if}
</div>
