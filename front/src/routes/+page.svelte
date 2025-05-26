<script lang="ts">
	import { onMount } from 'svelte';
	import UserLocation from './components/UserLocation.svelte';
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';

	let todayPlans: components['schemas']['TripPlanDto'][] = [];
	let hourlyForecast: components['schemas']['WeatherInfoDto'][] = [];
	let locationText = '';
	let nickname = rq.member.nickname + '님' || ' ';

	onMount(async () => {
		const planRes = await rq.apiEndPoints().GET('/api/v1/trip/today');
		if (!planRes.error) {
			todayPlans = planRes.data?.data ?? [];
		}
	});

	async function handleLocation(info: { siDo: string; siGunGu: string; dong: string }) {
		locationText = `${info.siDo} ${info.siGunGu} ${info.dong}`;
		const address = locationText;

		const { data, error } = await rq.apiEndPoints().GET('/api/v1/weather/current', {
			params: { query: { address } }
		});
		if (!error) {
			hourlyForecast = data?.data ?? [];
		}
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

	function beforeDash(text: string): string {
		return text.split(' - ')[0].trim();
	}

	function getPlaceName(name: string, type: string): string {
		name = name.replace(/\([^)]*\)/g, '');
		const suffix = type === 'FLIGHT' ? '공항' : type === 'TRAIN' ? '역' : '고속버스터미널';
		return name.trim() + suffix;
	}

	function getIconClass(type: string): string {
		return type === 'FLIGHT'
			? 'fa-solid fa-plane-departure'
			: type === 'TRAIN'
				? 'fa-solid fa-train'
				: 'fa-solid fa-bus';
	}
</script>

<div class="p-1 space-y-2">
	<h1 class="text-lg font-bold">
		<i class="mr-1 fa-solid fa-hands-clapping fa-bounce" style="color: #FFD43B;"></i> 안녕하세요 {nickname}
	</h1>

	<!-- 사용자 위치 조회 -->
	<UserLocation onLocationFound={handleLocation} />

	<!-- 오늘의 플랜 (슬라이드) -->
	<div class="bg-blue-50 border-l-4 border-blue-500 p-4 rounded">
		<h2 class="font-semibold mb-1">
			<i class="fa-solid fa-calendar-check mx-1 text-blue-900"></i> 오늘의 플랜
		</h2>

		<hr class="border-t border-gray-300 mb-3" />

		{#if todayPlans.length > 0}
			<div class="flex gap-4 overflow-x-auto pb-2">
				{#each todayPlans as plan}
					<div class="min-w-[250px] flex-shrink-0 space-y-1">
						<p class="font-bold text-black">{plan.planName}</p>
						<p class="text-sm text-black">
							{beforeDash(plan.startAddress)} → {beforeDash(plan.endAddress)}
						</p>
						<p class="text-sm text-gray-700 flex items-center gap-1">
							<i class={`${getIconClass(plan.legs[0]?.transportType)} text-blue-900`}></i>
							{plan.legs[0]?.departureTime?.slice(11, 16)} 출발
						</p>
						<p class="text-sm text-gray-700">
							{getPlaceName(plan.legs[0]?.departureName, plan.legs[0]?.transportType)} →
							{getPlaceName(plan.legs[0]?.arrivalName, plan.legs[0]?.transportType)}
						</p>
					</div>
				{/each}
			</div>
		{:else}
			<p class="text-sm text-gray-500">오늘은 등록된 플랜이 없습니다.</p>
		{/if}
	</div>

	<!-- 날씨 정보 -->
	<div class="bg-yellow-50 border-l-4 border-yellow-500 p-4 rounded">
		<h2 class="font-semibold mb-1">
			<i class="fa-solid fa-cloud-sun mr-1 text-blue-900"></i>{locationText}
		</h2>
		{#if hourlyForecast.length > 0}
			<div class="border-b border-gray-300 px-2 py-4 flex gap-4 overflow-x-auto">
				{#each hourlyForecast as info}
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
			<div class="flex justify-end text-sm mt-2">출처 : 기상청</div>
		{/if}
	</div>
</div>
