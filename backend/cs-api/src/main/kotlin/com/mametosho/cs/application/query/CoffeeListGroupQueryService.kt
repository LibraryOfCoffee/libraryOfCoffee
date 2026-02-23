package com.mametosho.cs.application.query

import com.mametosho.cs.application.query.result.CoffeeListGroupDetailResult

/**
 * 珈琲リストグループの読み取り専用クエリサービス。
 */
interface CoffeeListGroupQueryService {

    /**
     * 指定されたIDの珈琲リストグループ詳細を取得する。
     *
     * @param id 珈琲リストグループID
     * @return グループ詳細。存在しない場合はnull
     */
    fun findDetailById(id: String): CoffeeListGroupDetailResult?
}
