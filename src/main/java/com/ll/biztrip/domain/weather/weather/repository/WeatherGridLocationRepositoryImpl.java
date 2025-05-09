package com.ll.biztrip.domain.weather.weather.repository;

import com.ll.biztrip.domain.weather.weather.entity.QWeatherGridLocation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class WeatherGridLocationRepositoryImpl implements CustomWeatherGridLocationRepository {

    private final JPAQueryFactory queryFactory;

    QWeatherGridLocation location = QWeatherGridLocation.weatherGridLocation;

    @Override
    public Optional<String> findBestLocationCode(String siDo, String siGunGu) {

        String code = queryFactory.select(location.locationCode)
                .from(location)
                .where(
                        location.siDo.startsWith(siDo),
                        location.siGunGu.startsWith(siGunGu)
                )
                .fetchFirst();

        if (code == null) {
            code = queryFactory.select(location.locationCode)
                    .from(location)
                    .where(
                            location.siDo.startsWith(siDo),
                            location.siGunGu.isNull()
                    )
                    .fetchFirst();
        }

        return Optional.ofNullable(code);
    }

}
