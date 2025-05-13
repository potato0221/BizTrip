<script lang="ts">
	import { onMount } from 'svelte';

	export let onLocationFound: (info: {
		siDo: string;
		siGunGu: string;
		dong: string;
	}) => void = () => {};

	let locationInfo = {
		siDo: '',
		siGunGu: '',
		dong: ''
	};

	async function fetchKakaoRegion(lat: number, lon: number) {
		const KAKAO_KEY = import.meta.env.VITE_KAKAO_REST_KEY;

		const res = await fetch(
			`https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=${lon}&y=${lat}`,
			{
				headers: {
					Authorization: `KakaoAK ${KAKAO_KEY}`
				}
			}
		);

		const data = await res.json();
		const region = data.documents[0];

		locationInfo = {
			siDo: region.region_1depth_name,
			siGunGu: region.region_2depth_name,
			dong: region.region_3depth_name
		};

		onLocationFound(locationInfo);
	}

	function getUserLocation() {
		if (!navigator.geolocation) {
			alert('위치 정보 사용이 불가능합니다.');
			return;
		}

		navigator.geolocation.getCurrentPosition(
			(pos) => {
				const { latitude, longitude } = pos.coords;
				fetchKakaoRegion(latitude, longitude);
			},
			(err) => {
				alert('위치 정보를 사용할 수 없습니다.');
			}
		);
	}

	onMount(() => {
		getUserLocation();
	});
</script>

<p class="text-sm"></p>
