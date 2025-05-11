<script lang="ts">
	import PlanSelector from './components/WeatherPlanList.svelte';
	import DailySummaryItem from './components/DailySummaryItem.svelte';
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';

	let selectedPlanId: number | null = null;

	let todayForecast: components['schemas']['WeatherInfoDto'][] = [];
	let dailySummary: components['schemas']['DailySummaryDto'][] = [];

	async function fetchWeather(id: number) {
		const { data, error } = await rq.apiEndPoints().GET('/api/v1/weather/trip', {
			params: { query: { tripPlanId: id } }
		});

		if (error) {
			rq.msgError(error.msg);
			return;
		}

		const response: components['schemas']['WeatherResponseDto'] | undefined = data?.data;

		todayForecast = response?.hourly ?? [];
		dailySummary = response?.daily ?? [];
	}

	function handlePlanChange(id: number) {
		selectedPlanId = id;
		fetchWeather(id);
	}

	function iconForSky(sky: string): string {
		if (!sky) return 'fa-solid fa-question';
		if (sky.includes('맑')) return 'fa-solid fa-sun text-[#f33f12]';
		if (sky.includes('구름')) return 'fa-solid fa-cloud-sun text-[#FFD43B]';
		if (sky.includes('흐림')) return 'fa-solid fa-cloud text-[#74C0FC]';
		if (sky.includes('비')) return 'fa-solid fa-cloud-showers-heavy text-[#1264f3]';
		if (sky.includes('눈')) return 'fa-solid fa-cloud-meatball text-[#29a2ff]';
		return 'fa-solid fa-question';
	}
</script>

<h1 class="text-xl font-bold mb-4">
	<i class="fa-solid fa-sun" style="color: #f33f12;"></i> 도착지 날씨 확인
</h1>

<PlanSelector {selectedPlanId} onSelectedPlanChange={handlePlanChange} />

{#if todayForecast.length > 0}
	<h2 class="mt-6 text-lg font-semibold">
		{todayForecast[0]?.localName ?? '알 수 없는 지역'}
	</h2>
	<div class="border-b border-gray-300 px-2 py-4 flex gap-4 overflow-x-auto">
		{#each todayForecast as info}
			<div class="min-w-[80px] text-center text-sm px-2">
				<div class="text-xs text-gray-500">{info.time}</div>
				<div class="text-2xl"><i class={iconForSky(info.sky)}></i></div>
				<div class="text-xs">
					<i class="fa-solid fa-droplet mr-1" style="color: #86cafe;"></i>
					{info.rainProb}%
				</div>
				<div class="text-sm font-semibold mt-1">{info.temperature}℃</div>
			</div>
		{/each}
	</div>

	<h2 class="mt-6 text-lg font-semibold">📅 날짜별 요약</h2>

	<div class="mt-2 space-y-2">
		{#each dailySummary.slice(0, 5) as day}
			<DailySummaryItem {day} />
		{/each}
	</div>
{:else if selectedPlanId}
	<p class="text-gray-500 mt-6">
		<i class="fa-solid fa-hourglass-half"></i> 날씨 정보를 불러오는 중...
	</p>
{/if}
