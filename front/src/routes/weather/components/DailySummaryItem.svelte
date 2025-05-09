<script lang="ts">
	import type { components } from '$lib/types/api/v1/schema';

	export let day: components['schemas']['DailySummaryDto'];

	function emojiForSky(sky: string): string {
		if (!sky) return '❓';
		if (sky.includes('맑')) return '☀️';
		if (sky.includes('구름')) return '⛅';
		if (sky.includes('흐림')) return '☁️';
		if (sky.includes('비')) return '🌧️';
		if (sky.includes('눈')) return '🌨️';
		if (sky.includes('안개')) return '🌫️';
		return '❓';
	}

	function getDayKor(dateStr: string): string {
		const date = new Date(dateStr);
		const daysKor = ['일', '월', '화', '수', '목', '금', '토'];
		return `${daysKor[date.getDay()]}요일`;
	}
</script>

<div class="flex justify-between items-center text-sm px-2 py-1">
	<div class="w-14 font-semibold">{getDayKor(day.date)}</div>
	<div class="w-14 text-center">💧 {day.rainProb ?? '-'}%</div>
	<div class="w-8 text-center">{emojiForSky(day.amSky)}</div>
	<div class="w-8 text-center">{emojiForSky(day.pmSky)}</div>
	<div class="w-12 text-right font-bold">{day.maxTemp ?? '-'}℃</div>
	<div class="w-12 text-right text-gray-500">{day.minTemp ?? '-'}℃</div>
</div>
