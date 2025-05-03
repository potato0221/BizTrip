<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';

	let selectedTab = 'bus';
	let terminalList: components['schemas']['TerminalDto'][] = [];
	let loading = false;

	let departureDate = '';
	let departure = '';
	let arrival = '';

	let filteredDepartureList: components['schemas']['TerminalDto'][] = [];
	let filteredArrivalList: components['schemas']['TerminalDto'][] = [];

	let showDepartureList = false;
	let showArrivalList = false;

	let busSchedules: components['schemas']['BusScheduleDto'][] = [];

	async function loadBusTerminals() {
		loading = true;
		const { data } = await rq.apiEndPoints().GET('/api/v1/travel/bus/terminalList');
		terminalList = data?.data ?? [];
		loading = false;
	}

	rq.effect(() => {
		if (selectedTab === 'bus') {
			loadBusTerminals();
		}
	});

	function filterDepartureList() {
		showDepartureList = true;
		filteredDepartureList = terminalList.filter((t) => t.terminalName.includes(departure));
	}

	function filterArrivalList() {
		showArrivalList = true;
		filteredArrivalList = terminalList.filter((t) => t.terminalName.includes(arrival));
	}

	function selectDeparture(name: string) {
		departure = name;
		showDepartureList = false;
	}

	function selectArrival(name: string) {
		arrival = name;
		showArrivalList = false;
	}

	function formatDateOnly(str: string): string {
		const month = parseInt(str.slice(4, 6));
		const day = parseInt(str.slice(6, 8));
		return `${month}월 ${day}일`;
	}

	function formatTimeOnly(str: string): string {
		const hour = str.slice(8, 10);
		const minute = str.slice(10, 12);
		return `${hour}시 ${minute}분`;
	}

	async function searchRoute() {
		if (!departure || !arrival || !departureDate) {
			alert('출발일, 출발지, 도착지를 모두 입력하세요.');
			return;
		}

		const departureTerminal = terminalList.find((t) => t.terminalName === departure);
		const arrivalTerminal = terminalList.find((t) => t.terminalName === arrival);

		if (!departureTerminal || !arrivalTerminal) {
			alert('입력하신 터미널명을 다시 확인해주세요.');
			return;
		}

		const { data, error } = await rq.apiEndPoints().GET('/api/v1/travel/bus/schedule', {
			params: {
				query: {
					departureTerminalId: departureTerminal.terminalId,
					arrivalTerminalId: arrivalTerminal.terminalId,
					departureDate: departureDate.replace(/-/g, '')
				}
			}
		});

		busSchedules = data?.data ?? [];
		console.log(busSchedules);
	}
</script>

<!-- 탭 UI -->
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

<!-- 콘텐츠 -->
<div class="mx-auto w-full max-w-lg px-4">
	{#if selectedTab === 'bus'}
		{#if loading}
			<p class="text-center"><i class="fa-solid fa-spinner mr-2"></i>로딩 중...</p>
		{:else}
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
					<!-- 출발역 -->
					<div class="flex-1 relative">
						<label class="block mb-1 text-lg font-bold">출발역</label>
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
								{#each filteredDepartureList as terminal}
									<li
										class="px-3 py-2 hover:bg-gray-100 cursor-pointer border-b border-gray-200 last:border-0"
										on:click={() => selectDeparture(terminal.terminalName)}
									>
										{terminal.terminalName}
									</li>
								{/each}
							</ul>
						{/if}
					</div>

					<!-- 🔁 리버스 버튼 -->
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

					<!-- 도착역 -->
					<div class="flex-1 relative">
						<label class="block mb-1 text-lg font-bold">도착역</label>
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
								{#each filteredArrivalList as terminal}
									<li
										class="px-3 py-2 hover:bg-gray-100 cursor-pointer border-b border-gray-200 last:border-0"
										on:click={() => selectArrival(terminal.terminalName)}
									>
										{terminal.terminalName}
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

				<!-- ✅ 조회 결과 리스트 -->
				{#if busSchedules.length > 0}
					<h3 class="text-lg font-bold mt-6 mb-2">
						{formatDateOnly(busSchedules[0].depPlandTime)}
						{busSchedules[0].depPlaceNm} → {busSchedules[0].arrPlaceNm}
					</h3>
					<ul class="space-y-2">
						{#each busSchedules as schedule}
							<li class="border border-gray-300 rounded p-3 shadow-sm">
								<p class="text-sm text-gray-700">출발: {formatTimeOnly(schedule.depPlandTime)}</p>
								<p class="text-sm text-gray-700">도착: {formatTimeOnly(schedule.arrPlandTime)}</p>
								<p class="text-sm text-gray-600">등급: {schedule.gradeNm}</p>
							</li>
						{/each}
					</ul>
				{:else if departure && arrival && departureDate}
					<p class="mt-6 text-center text-gray-500">조회된 노선이 없습니다.</p>
				{/if}
			</div>
		{/if}
	{:else if selectedTab === 'ktx'}
		<p>KTX 탭</p>
	{:else if selectedTab === 'flight'}
		<p>항공편 탭</p>
	{/if}
</div>
