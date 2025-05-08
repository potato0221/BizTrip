<script lang="ts">
	import { onMount } from 'svelte';
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';

	let stationList: components['schemas']['StationDto'][] = [];
	let trainSchedules: components['schemas']['TrainScheduleDto'][] = [];
	let trainTypes: components['schemas']['TrainTypeDto'][] = [];

	let filteredDepartureList: components['schemas']['StationDto'][] = [];
	let filteredArrivalList: components['schemas']['StationDto'][] = [];

	let departureDate = '';
	let departure = '';
	let arrival = '';
	let trainType = '00';
	let selectedIndex: number | null = null;

	let showDepartureList = false;
	let showArrivalList = false;

	onMount(() => {
		loadStations();
		loadTrainTypes();
	});

	async function loadStations() {
		const { data } = await rq.apiEndPoints().GET('/api/v1/travel/train/stationList');
		stationList = data?.data ?? [];
	}

	async function loadTrainTypes() {
		const { data } = await rq.apiEndPoints().GET('/api/v1/travel/train/trainType');
		trainTypes = data?.data ?? [];
	}

	function filterDepartureList() {
		showDepartureList = true;
		filteredDepartureList = stationList.filter((s) => s.stationName.includes(departure));
	}

	function filterArrivalList() {
		showArrivalList = true;
		filteredArrivalList = stationList.filter((s) => s.stationName.includes(arrival));
	}

	function selectDeparture(name: string) {
		departure = name;
		showDepartureList = false;
	}

	function selectArrival(name: string) {
		arrival = name;
		showArrivalList = false;
	}

	function formatTimeOnly(str?: string): string {
		if (!str || str.length < 12) return '-';
		const hour = str.slice(8, 10);
		const minute = str.slice(10, 12);
		return `${hour}시 ${minute}분`;
	}

	function formatDateOnly(str: string): string {
		const month = parseInt(str.slice(4, 6));
		const day = parseInt(str.slice(6, 8));
		return `${month}월 ${day}일`;
	}

	async function searchRoute() {
		if (!departure || !arrival || !departureDate) {
			rq.msgError('출발일, 출발지, 도착지를 모두 입력하세요.');
			return;
		}

		const departureStation = stationList.find((s) => s.stationName === departure);
		const arrivalStation = stationList.find((s) => s.stationName === arrival);

		if (!departureStation || !arrivalStation) {
			rq.msgError('입력하신 역명을 다시 확인해주세요.');
			return;
		}

		const { data } = await rq.apiEndPoints().GET('/api/v1/travel/train/schedule', {
			params: {
				query: {
					departureStationId: departureStation.stationId,
					arrivalStationId: arrivalStation.stationId,
					departureDate: departureDate.replace(/-/g, ''),
					trainType: trainType
				}
			}
		});

		trainSchedules = data?.data ?? [];
		selectedIndex = null;
	}

	function toISO(str: string): string {
		return `${str.slice(0, 4)}-${str.slice(4, 6)}-${str.slice(6, 8)}T${str.slice(8, 10)}:${str.slice(10, 12)}:${str.slice(12, 14)}`;
	}

	async function registerSchedule() {
		const selected = trainSchedules[selectedIndex!];

		const { data, error } = await rq.apiEndPoints().POST('/api/v1/travel/train/register', {
			body: {
				departureTime: toISO(selected.depplandtime),
				arrivalTime: toISO(selected.arrplandtime),
				departureName: selected.depplacename,
				arrivalName: selected.arrplacename,
				trainType: selected.traingradename
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

	<!-- 출발역/도착역 -->
	<div class="flex gap-2 relative items-end">
		<div class="flex-1 relative">
			<label class="block mb-1 text-lg font-bold">출발역</label>
			<input
				type="text"
				bind:value={departure}
				on:input={filterDepartureList}
				on:focus={() => (showDepartureList = true)}
				on:blur={() => setTimeout(() => (showDepartureList = false), 100)}
				placeholder="출발역"
				class="input input-bordered w-full focus:border-blue-900 focus:border-2 focus:outline-none"
			/>
			{#if showDepartureList && filteredDepartureList.length > 0}
				<ul
					class="absolute z-10 bg-white border border-gray-300 w-full max-h-40 overflow-y-auto mt-1 rounded shadow"
				>
					{#each filteredDepartureList as station}
						<li
							class="px-3 py-2 hover:bg-gray-100 cursor-pointer border-b border-gray-200 last:border-0"
							on:click={() => selectDeparture(station.stationName)}
						>
							{station.stationName}
						</li>
					{/each}
				</ul>
			{/if}
		</div>

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

		<div class="flex-1 relative">
			<label class="block mb-1 text-lg font-bold">도착역</label>
			<input
				type="text"
				bind:value={arrival}
				on:input={filterArrivalList}
				on:focus={() => (showArrivalList = true)}
				on:blur={() => setTimeout(() => (showArrivalList = false), 100)}
				placeholder="도착역"
				class="input input-bordered w-full focus:border-blue-900 focus:border-2 focus:outline-none"
			/>
			{#if showArrivalList && filteredArrivalList.length > 0}
				<ul
					class="absolute z-10 bg-white border border-gray-300 w-full max-h-40 overflow-y-auto mt-1 rounded shadow"
				>
					{#each filteredArrivalList as station}
						<li
							class="px-3 py-2 hover:bg-gray-100 cursor-pointer border-b border-gray-200 last:border-0"
							on:click={() => selectArrival(station.stationName)}
						>
							{station.stationName}
						</li>
					{/each}
				</ul>
			{/if}
		</div>
	</div>

	<!-- 기차종류 + 조회버튼 -->
	<div class="flex gap-2 items-end">
		<div class="flex-1">
			<label class="block mb-1 text-lg font-bold">기차 종류</label>
			<select
				bind:value={trainType}
				class="select select-bordered w-full focus:border-blue-900 focus:ring-1 focus:ring-blue-900 focus:outline-none max-h-40 overflow-y-auto"
			>
				{#each trainTypes as type}
					<option value={type.trainId}>{type.trainName}</option>
				{/each}
			</select>
		</div>
		<button
			on:click={searchRoute}
			class="px-4 py-[0.55rem] text-sm font-bold text-blue-900 border-2 border-blue-900 rounded-lg bg-transparent hover:bg-blue-900 hover:text-white focus:bg-blue-900 focus:text-white transition-all"
		>
			조회
		</button>
	</div>

	<!-- 조회 결과 -->
	{#if trainSchedules.length > 0}
		<h3 class="text-lg font-bold mt-6 mb-2">
			{formatDateOnly(trainSchedules[0].depplandtime)}
			{trainSchedules[0].depplacename} → {trainSchedules[0].arrplacename}
		</h3>
		<ul class="space-y-2">
			{#each trainSchedules as schedule, idx}
				<li
					class="border rounded p-3 shadow-sm transition-all cursor-pointer hover:bg-gray-50"
					class:border-blue-900={selectedIndex === idx}
					class:border-2={selectedIndex === idx}
					on:click={() => (selectedIndex = idx)}
				>
					<p class="text-sm text-gray-700">출발: {formatTimeOnly(schedule.depplandtime)}</p>
					<p class="text-sm text-gray-700">도착: {formatTimeOnly(schedule.arrplandtime)}</p>
					<p class="text-sm text-gray-600">등급: {schedule.traingradename}</p>

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
		<p class="mt-6 text-center text-gray-500">조회된 기차 스케줄이 없습니다.</p>
	{/if}
</div>
