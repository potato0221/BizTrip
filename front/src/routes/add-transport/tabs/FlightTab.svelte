<script lang="ts">
	import { onMount } from 'svelte';
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';

	type Airport = components['schemas']['AirportDto'];
	type Airline = components['schemas']['AirlineDto'];
	type FlightSchedule = components['schemas']['FlightScheduleDto'];

	let airportList: Airport[] = [];
	let airlineList: Airline[] = [];
	let schedules: FlightSchedule[] = [];

	let departure = '';
	let arrival = '';
	let departureDate = '';
	let airlineNm = '전체';
	let selectedIndex: number | null = null;

	let showDepartureList = false;
	let showArrivalList = false;
	let filteredDepartureList: Airport[] = [];
	let filteredArrivalList: Airport[] = [];

	onMount(() => {
		loadAirports();
		loadAirlines();
	});

	async function loadAirports() {
		const { data } = await rq.apiEndPoints().GET('/api/v1/transport/flight/airport');
		airportList = data?.data ?? [];
	}

	async function loadAirlines() {
		const { data } = await rq.apiEndPoints().GET('/api/v1/transport/flight/airline');
		airlineList = data?.data ?? [];
	}

	function filterDepartureList() {
		showDepartureList = true;
		filteredDepartureList = airportList.filter((a) => a.airportName.includes(departure));
	}

	function filterArrivalList() {
		showArrivalList = true;
		filteredArrivalList = airportList.filter((a) => a.airportName.includes(arrival));
	}

	function selectDeparture(name: string) {
		departure = name;
		showDepartureList = false;
	}

	function selectArrival(name: string) {
		arrival = name;
		showArrivalList = false;
	}

	function formatTimeOnly(str: string): string {
		const hour = str.slice(8, 10);
		const minute = str.slice(10, 12);
		return `${hour}시 ${minute}분`;
	}

	function formatDateOnly(str: string): string {
		const month = parseInt(str.slice(4, 6));
		const day = parseInt(str.slice(6, 8));
		return `${month}월 ${day}일`;
	}

	function toISO(str: string): string {
		return `${str.slice(0, 4)}-${str.slice(4, 6)}-${str.slice(6, 8)}T${str.slice(8, 10)}:${str.slice(10, 12)}:${str.slice(12, 14) || '00'}`;
	}

	async function searchRoute() {
		if (!departure || !arrival || !departureDate) {
			rq.msgError('출발일, 출발지, 도착지를 모두 입력하세요.');
			return;
		}

		const dep = airportList.find((a) => a.airportName === departure);
		const arr = airportList.find((a) => a.airportName === arrival);
		const airline = airlineList.find((a) => a.airlineName === airlineNm);

		if (!dep || !arr) {
			rq.msgError('입력하신 공항명을 다시 확인해주세요.');
			return;
		}

		const { data } = await rq.apiEndPoints().GET('/api/v1/transport/flight/schedule', {
			params: {
				query: {
					departureAirportId: dep.airportId,
					arrivalAirportId: arr.airportId,
					departureDate: departureDate.replace(/-/g, ''),
					airlineId: airlineNm === '전체' ? 'all' : (airline?.airlineId ?? '')
				}
			}
		});

		schedules = data?.data ?? [];

		console.log(data);
		selectedIndex = null;
	}

	async function registerSchedule() {
		if (selectedIndex === null) return;
		const selected = schedules[selectedIndex];

		const { data, error } = await rq.apiEndPoints().POST('/api/v1/transport/flight/register', {
			body: {
				departureName: selected.depAirportNm,
				arrivalName: selected.arrAirportNm,
				departureTime: toISO(selected.depPlandTime),
				arrivalTime: toISO(selected.arrPlandTime),
				flightNumber: selected.vihicleId,
				airline: selected.airlineNm
			}
		});

		if (data) {
			rq.msgInfo(data.msg);
		} else {
			rq.msgError(error.msg);
		}
	}
</script>

<div class="mb-4 space-y-4">
	<!-- 출발일 -->
	<div>
		<label class="block mb-1 text-lg font-bold">출발일</label>
		<div
			class="w-full rounded border border-gray-300 focus-within:border-blue-900 focus-within:ring-1 focus-within:ring-blue-900 px-3 py-2 transition-all"
		>
			<input
				type="date"
				bind:value={departureDate}
				class="w-full outline-none bg-transparent [&::-webkit-calendar-picker-indicator]:cursor-pointer"
			/>
		</div>
	</div>

	<!-- 출발지/도착지 + 조회 -->
	<div class="flex gap-2 relative items-end">
		<!-- 출발지 -->
		<div class="flex-1 relative">
			<label class="block mb-1 text-lg font-bold">출발 공항</label>
			<input
				type="text"
				bind:value={departure}
				on:input={filterDepartureList}
				on:focus={() => (showDepartureList = true)}
				on:blur={() => setTimeout(() => (showDepartureList = false), 100)}
				placeholder="출발지"
				class="input input-bordered w-full focus:border-blue-900 focus:border-2 focus:outline-none"
			/>
			{#if showDepartureList && filteredDepartureList.length > 0}
				<ul
					class="absolute z-10 bg-white border border-gray-300 w-full max-h-40 overflow-y-auto mt-1 rounded shadow"
				>
					{#each filteredDepartureList as airport}
						<li
							class="px-3 py-2 hover:bg-gray-100 cursor-pointer border-b border-gray-200 last:border-0"
							on:click={() => selectDeparture(airport.airportName)}
						>
							{airport.airportName}
						</li>
					{/each}
				</ul>
			{/if}
		</div>

		<!-- 리버스 버튼 -->
		<div class="flex items-center h-full pt-6">
			<button
				on:click={() => {
					const temp = departure;
					departure = arrival;
					arrival = temp;
				}}
				class="text-blue-900 hover:text-white hover:bg-blue-900 border border-blue-900 rounded px-2 py-[0.45rem] transition-all"
				title="출발/도착 교체"
			>
				<i class="fa-solid fa-arrows-left-right"></i>
			</button>
		</div>

		<!-- 도착지 -->
		<div class="flex-1 relative">
			<label class="block mb-1 text-lg font-bold">도착 공항</label>
			<input
				type="text"
				bind:value={arrival}
				on:input={filterArrivalList}
				on:focus={() => (showArrivalList = true)}
				on:blur={() => setTimeout(() => (showArrivalList = false), 100)}
				placeholder="도착지"
				class="input input-bordered w-full focus:border-blue-900 focus:border-2 focus:outline-none"
			/>
			{#if showArrivalList && filteredArrivalList.length > 0}
				<ul
					class="absolute z-10 bg-white border border-gray-300 w-full max-h-40 overflow-y-auto mt-1 rounded shadow"
				>
					{#each filteredArrivalList as airport}
						<li
							class="px-3 py-2 hover:bg-gray-100 cursor-pointer border-b border-gray-200 last:border-0"
							on:click={() => selectArrival(airport.airportName)}
						>
							{airport.airportName}
						</li>
					{/each}
				</ul>
			{/if}
		</div>

		<!-- 조회 버튼 -->
		<div>
			<button
				class="px-4 py-[0.55rem] text-sm font-bold text-blue-900 border-2 border-blue-900 rounded-lg bg-transparent hover:bg-blue-900 hover:text-white focus:bg-blue-900 focus:text-white transition-all"
				on:click={searchRoute}
			>
				조회
			</button>
		</div>
	</div>

	<!-- 항공사 선택 -->
	<div>
		<label class="block mb-1 text-lg font-bold">항공사</label>
		<select
			bind:value={airlineNm}
			class="select select-bordered w-full focus:border-blue-900 focus:ring-1 focus:ring-blue-900 focus:outline-none max-h-40 overflow-y-auto"
		>
			<option value="전체">전체</option>
			{#each airlineList as airline}
				<option value={airline.airlineName}>{airline.airlineName}</option>
			{/each}
		</select>
	</div>

	<!-- 조회 결과 리스트 -->
	{#if schedules.length > 0}
		<h3 class="text-lg font-bold mt-6 mb-2">
			{formatDateOnly(schedules[0].depPlandTime)}
			{schedules[0].depAirportNm} → {schedules[0].arrAirportNm}
		</h3>
		<ul class="space-y-2">
			{#each schedules as schedule, idx}
				<li
					class="border rounded p-3 shadow-sm transition-all cursor-pointer hover:bg-gray-50"
					class:border-blue-900={selectedIndex === idx}
					class:border-2={selectedIndex === idx}
					on:click={() => (selectedIndex = idx)}
				>
					<p class="text-sm text-gray-700">출발: {formatTimeOnly(schedule.depPlandTime)}</p>
					<p class="text-sm text-gray-700">도착: {formatTimeOnly(schedule.arrPlandTime)}</p>
					<p class="text-sm text-gray-600">항공편: {schedule.vihicleId}</p>
					<p class="text-sm text-gray-600">항공사: {schedule.airlineNm}</p>
					{#if selectedIndex === idx}
						<div class="flex justify-end">
							<button
								on:click|stopPropagation={registerSchedule}
								class="mt-2 px-3 py-1 text-sm font-semibold text-blue-900 border border-blue-900 rounded hover:bg-blue-900 hover:text-white"
							>
								등록
							</button>
						</div>
					{/if}
				</li>
			{/each}
		</ul>
	{:else if departure && arrival && departureDate}
		<p class="mt-6 text-center text-gray-500">조회된 항공편이 없습니다.</p>
	{/if}
</div>
