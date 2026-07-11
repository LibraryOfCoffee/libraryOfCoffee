package com.mametosho.domain.model.shared

/**
 * 参画ステータス。
 *
 * BEFORE_PARTICIPATION（参画前）→ PARTICIPATING（参画中）→ DROPPED（参画落ち）の直線遷移のみ許可。
 * DROPPED になると以降のステータス変更は不可。
 * PARTICIPATING の店舗のみ CS（一般ユーザー向け）に公開される。
 */
enum class ParticipationStatus {
    BEFORE_PARTICIPATION,
    PARTICIPATING,
    DROPPED,
}
