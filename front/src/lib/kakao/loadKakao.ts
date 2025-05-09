export function loadKakaoSdk(): Promise<void> {
	return new Promise((resolve, reject) => {
		if (typeof window === 'undefined') return reject('Client only');

		if (window.kakao && window.kakao.maps && window.kakao.maps.services) {
			return resolve();
		}

		const script = document.createElement('script');
		script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${
			import.meta.env.VITE_KAKAO_JS_KEY
		}&autoload=false&libraries=services`;
		script.async = true;

		script.onload = () => {
			if (!window.kakao || !window.kakao.maps) return reject('SDK loaded but kakao.maps not found');
			window.kakao.maps.load(() => {
				if (window.kakao.maps.services) {
					resolve();
				} else {
					console.warn('⚠️ services 라이브러리 아직 로딩 안됨, 100ms 후 재확인');
					let attempts = 0;
					const check = () => {
						if (window.kakao.maps.services) {
							resolve();
						} else if (attempts++ > 10) {
							reject('services 라이브러리 로딩 실패');
						} else {
							setTimeout(check, 100);
						}
					};
					check();
				}
			});
		};

		script.onerror = reject;
		document.head.appendChild(script);
	});
}
