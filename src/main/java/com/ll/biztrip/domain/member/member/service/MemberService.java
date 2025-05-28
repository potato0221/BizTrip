package com.ll.biztrip.domain.member.member.service;

import com.ll.biztrip.domain.member.member.dto.NicknameDto;
import com.ll.biztrip.domain.member.member.entity.Member;
import com.ll.biztrip.domain.member.member.repository.MemberRepository;
import com.ll.biztrip.global.enums.Msg;
import com.ll.biztrip.global.exceptions.GlobalException;
import com.ll.biztrip.global.rq.Rq;
import com.ll.biztrip.global.rsData.RsData;
import com.ll.biztrip.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;
    private final Rq rq;

    @Transactional
    public RsData<Member> join(String username, String password) {
        return join(username, password, username, "");
    }

    @Transactional
    public RsData<Member> join(String username, String password, String nickname, String profileImgUrl) {
        if (findByUsername(username).isPresent()) {
            return RsData.of(Msg.E400_5_ALREADY_REGISTERED_MEMBER);
        }

        Member member = Member.builder()
                .username(username)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .password(passwordEncoder.encode(password))
                .refreshToken(authTokenService.genRefreshToken())
                .build();
        memberRepository.save(member);

        return RsData.of(Msg.E200_0_CREATE_SUCCEED, member);
    }

    @Transactional
    public RsData<Member> modifyOrJoin(String username, String providerTypeCode, String nickname, String profileImgUrl) {
        Member member = findByUsername(username).orElse(null);

        if (member == null) {
            return join(
                    username, "", nickname, profileImgUrl
            );
        }

        return modify(member, profileImgUrl);
    }

    @Transactional
    public RsData<Member> modify(Member member, String profileImgUrl) {
        member.setProfileImgUrl(profileImgUrl);

        return RsData.of(Msg.E200_2_MODIFY_SUCCEED, member);
    }


    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public long count() {
        return memberRepository.count();
    }

    public RsData<Member> whenSocialLogin(String providerTypeCode, String username, String nickname, String profileImgUrl) {
        Optional<Member> opMember = findByUsername(username);

        if (opMember.isPresent()) return RsData.of(Msg.E400_5_ALREADY_REGISTERED_MEMBER, opMember.get());

        return join(username, "");
    }

    @Transactional
    public Member modifyNickname(NicknameDto nicknameDto) {
        Member member = rq.getMember();

        member.setNickname(nicknameDto.getNickname());

        return memberRepository.save(member);
    }

    public record AuthAndMakeTokensResponseBody(
            @NonNull
            Member member,
            @NonNull
            String accessToken,
            @NonNull
            String refreshToken
    ) {}

    @Transactional
    public RsData<AuthAndMakeTokensResponseBody> authAndMakeTokens(String username, String password) {
        Member member = findByUsername(username)
                .orElseThrow(() -> new GlobalException(Msg.E400_7_NOT_FOUND_USER));

        if (!passwordMatches(member, password))
            throw new GlobalException(Msg.E400_6_INCORRECT_PASSWORD);

        String refreshToken = member.getRefreshToken();
        String accessToken = authTokenService.genAccessToken(member);

        return RsData.of(Msg.E200_5_LOGIN_SUCCEED,
                new AuthAndMakeTokensResponseBody(member, accessToken, refreshToken)
        );
    }

    @Transactional
    public String genAccessToken(Member member) {
        return authTokenService.genAccessToken(member);
    }

    public boolean passwordMatches(Member member, String password) {
        return passwordEncoder.matches(password, member.getPassword());
    }

    public SecurityUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = authTokenService.getDataFrom(accessToken);

        long id = (int) payloadBody.get("id");
        String username = (String) payloadBody.get("username");
        List<String> authorities = (List<String>) payloadBody.get("authorities");

        return new SecurityUser(
                id,
                username,
                "",
                authorities.stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

    public boolean validateToken(String token) {
        return authTokenService.validateToken(token);
    }

    public RsData<String> refreshAccessToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new GlobalException(Msg.E400_8_NOT_FOUND_REFRESH_TOKEN));

        String accessToken = authTokenService.genAccessToken(member);

        return RsData.of(Msg.E200_6_TOKEN_REFRESH_SUCCEED, accessToken);
    }
}
