<script lang="ts">
	import type { components } from '$lib/types/api/v1/schema';

	export let day: components['schemas']['DailySummaryDto'];

	function iconForSky(sky: string): string {
		if (!sky) return 'fa-solid fa-question';
		if (sky.includes('맑')) return 'fa-solid fa-sun text-[#f33f12]';
		if (sky.includes('구름')) return 'fa-solid fa-cloud-sun text-[#FFD43B]';
		if (sky.includes('흐림')) return 'fa-solid fa-cloud text-[#74C0FC]';
		if (sky.includes('비')) return 'fa-solid fa-cloud-showers-heavy text-[#1264f3]';
		if (sky.includes('눈')) return 'fa-solid fa-cloud-meatball text-[#29a2ff]';
		return 'fa-solid fa-question';
	}

	function getDayKor(dateStr: string): string {
		const date = new Date(dateStr);
		const daysKor = ['일', '월', '화', '수', '목', '금', '토'];
		return `${daysKor[date.getDay()]}요일`;
	}
</script>

<div class="flex justify-between items-center text-sm px-2 py-1">
	<div class="w-14 font-semibold">{getDayKor(day.date)}</div>
	<div class="w-14 text-center">
		<i class="fa-solid fa-droplet mr-1" style="color: #86cafe;"></i>
		{day.rainProb ?? '-'}%
	</div>
	<div class="w-8 text-center"><i class={iconForSky(day.amSky)}></i></div>
	<div class="w-8 text-center"><i class={iconForSky(day.pmSky)}></i></div>
	<div class="w-12 text-right font-bold">{day.maxTemp ?? '-'}℃</div>
	<div class="w-12 text-right text-gray-500">{day.minTemp ?? '-'}℃</div>
</div>
