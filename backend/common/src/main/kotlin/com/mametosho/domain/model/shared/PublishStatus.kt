package com.mametosho.domain.model.shared

/**
 * 公開状態。
 *
 * draft（下書き・非公開）と published（公開）の双方向の遷移が可能。
 * 下書きのデータはCS（一般ユーザー向け）には公開されない。
 */
enum class PublishStatus {
    DRAFT,
    PUBLISHED,
}
