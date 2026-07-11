package com.mametosho.domain.model.shared

/**
 * 公開状態。
 *
 * DRAFT（下書き・非公開）と PUBLISHED（公開）の双方向の遷移が可能。
 * INVALIDATED は店舗が参画落ちになった際にシステムが自動設定する終端状態。
 * INVALIDATED になったデータはそれ以降の変更は不可。
 * PUBLISHED 以外のデータは CS（一般ユーザー向け）には公開されない。
 */
enum class PublishStatus {
    DRAFT,
    PUBLISHED,
    INVALIDATED,
}
