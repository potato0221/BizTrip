<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';

	let myData: components['schemas']['MemberDto'][] = [];

	const addCity = async () => {
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/admin/addCity');

		if (data) {
			if (data.resultCode === '200-0') {
				rq.msgInfo('도시가 추가 되었습니다.');
			} else {
				rq.msgError(data.msg);
			}
		} else if (error) {
			rq.msgError('API 호출 오류');
		}
	};

	const addStation = async () => {
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/admin/addStation');

		if (data) {
			if (data.resultCode === '200-0') {
				rq.msgInfo('기차역이 추가 되었습니다.');
			} else {
				rq.msgError(data.msg);
			}
		} else if (error) {
			rq.msgError('API 호출 오류');
		}
	};

	const addTrainType = async () => {
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/admin/addTrainType');

		if (data) {
			if (data.resultCode === '200-0') {
				rq.msgInfo('기차 종류가 추가 되었습니다.');
			} else {
				rq.msgError(data.msg);
			}
		} else if (error) {
			rq.msgError('API 호출 오류');
		}
	};

	const addAirport = async () => {
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/admin/addAirport');

		if (data) {
			if (data.resultCode === '200-0') {
				rq.msgInfo('공항이 추가 되었습니다.');
			} else {
				rq.msgError(data.msg);
			}
		} else if (error) {
			rq.msgError('API 호출 오류');
		}
	};

	const addAirline = async () => {
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/admin/addAirline');

		if (data) {
			if (data.resultCode === '200-0') {
				rq.msgInfo('항공사가 추가 되었습니다.');
			} else {
				rq.msgError(data.msg);
			}
		} else if (error) {
			rq.msgError('API 호출 오류');
		}
	};

	const addTerminal = async () => {
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/admin/addTerminal');

		if (data) {
			if (data.resultCode === '200-0') {
				rq.msgInfo('터미널이 추가 되었습니다.');
			} else {
				rq.msgError(data.msg);
			}
		} else if (error) {
			rq.msgError('API 호출 오류');
		}
	};

	const addLocation = async () => {
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/admin/location');

		if (data) {
			if (data.resultCode === '200-0') {
				rq.msgInfo('위치 정보가 추가 되었습니다.');
			} else {
				rq.msgError(data.msg);
			}
		} else if (error) {
			rq.msgError('API 호출 오류');
		}
	};

	async function load() {
		const { data } = await rq.apiEndPoints().GET('/api/v1/members/me');

		myData = data?.data.item ?? [];

		return { myData };
	}
</script>

{#await load()}
	<h1>loading...</h1>
{:then { myData }}
	<div class="mx-auto w-full max-w-lg px-4 sm:px-6 lg:px-8">
		<div class="mx-auto flex flex max-w-4xl">
			<div>
				<p class="text-xl">관리자 페이지</p>
			</div>
		</div>
		<div class="flex">
			<div class="mr-2">
				<button
					on:click={addCity}
					class="inline-block rounded-md border border-gray-400 bg-white px-2 py-1 text-sm font-medium font-semibold text-gray-800 shadow-sm hover:bg-gray-700 hover:text-white focus:outline-none"
					>기차역 도시</button
				>
			</div>
			<div class="mr-2">
				<button
					on:click={addStation}
					class="inline-block rounded-md border border-gray-400 bg-white px-2 py-1 text-sm font-medium font-semibold text-gray-800 shadow-sm hover:bg-gray-700 hover:text-white focus:outline-none"
					>기차역</button
				>
			</div>
			<div class="mr-2">
				<button
					on:click={addTrainType}
					class="inline-block rounded-md border border-gray-400 bg-white px-2 py-1 text-sm font-medium font-semibold text-gray-800 shadow-sm hover:bg-gray-700 hover:text-white focus:outline-none"
					>기차 종류</button
				>
			</div>
		</div>
		<div class="flex mt-2">
			<div class="mr-2">
				<button
					on:click={addTerminal}
					class="inline-block rounded-md border border-gray-400 bg-white px-2 py-1 text-sm font-medium font-semibold text-gray-800 shadow-sm hover:bg-gray-700 hover:text-white focus:outline-none"
					>터미널</button
				>
			</div>
			<div class="mr-2">
				<button
					on:click={addAirport}
					class="inline-block rounded-md border border-gray-400 bg-white px-2 py-1 text-sm font-medium font-semibold text-gray-800 shadow-sm hover:bg-gray-700 hover:text-white focus:outline-none"
					>공항</button
				>
			</div>
			<div class="mr-2">
				<button
					on:click={addAirline}
					class="inline-block rounded-md border border-gray-400 bg-white px-2 py-1 text-sm font-medium font-semibold text-gray-800 shadow-sm hover:bg-gray-700 hover:text-white focus:outline-none"
					>항공사</button
				>
			</div>
			<div class="">
				<button
					on:click={addLocation}
					class="inline-block rounded-md border border-gray-400 bg-white px-2 py-1 text-sm font-medium font-semibold text-gray-800 shadow-sm hover:bg-gray-700 hover:text-white focus:outline-none"
					>위치</button
				>
			</div>
		</div>
	</div>
{/await}
