# ドメインモデル図

## 集約一覧

| 集約 | 概要 |
|------|------|
| [Administrator](domains/administrator.md) | 管理画面にログインする管理者 |
| [Customer](domains/customer.md) | 顧客とそのサブスクリプション契約 |
| [Plan](domains/plan.md) | 購入プラン（定期便・単品購入） |
| [MonthlySubscriptionDetail](domains/monthlySubscriptionDetail.md) | 月次の配送内容と発送ステータス |
| [Shop](domains/shop.md) | 珈琲豆を提供する店舗 |
| [CoffeeBean](domains/coffeeBean.md) | 店舗が提供する珈琲豆 |
| [Taste](domains/taste.md) | テイスト評価の種別（酸味・苦味など） |

## ドメインモデル図

```mermaid
classDiagram
  namespace Administrator集約 {
    class Administrator {
      <<Aggregate Root>>
      id: AdministratorId
      email: Email
      hashedPassword: String
      role: AdminRole
    }

    class AdminRole {
      <<Enum>>
      admin
      staff
    }

    class Email {
      <<Value Object>>
      value: String
    }
  }

  Administrator --> AdminRole
  Administrator --> Email

  namespace Customer集約 {
    class Customer {
      <<Aggregate Root>>
      id: CustomerId
      shopifyCustomerId: ShopifyCustomerId
      subscriptions: List~CustomerSubscription~
    }

    class CustomerSubscription {
      <<Entity>>
      id: CustomerSubscriptionId
      planId: PlanId
      status: SubscriptionStatus
      contractPeriod: ContractPeriod
    }

    class SubscriptionStatus {
      <<Enum>>
      active
      canceled
      ban
    }

    class ContractPeriod {
      <<Value Object>>
      from: LocalDate
      to: LocalDate?
    }
  }

  Customer *-- CustomerSubscription
  CustomerSubscription --> SubscriptionStatus
  CustomerSubscription --> ContractPeriod

  namespace Plan集約 {
    class Plan {
      <<Aggregate Root>>
      id: PlanId
      shopifyPlanId: ShopifyPlanId
      label: String
      gramWeight: Int
      beanQuantity: Int
      price: Int
      type: PlanType
      isRecommended: Boolean
    }

    class PlanType {
      <<Enum>>
      SUBSCRIPTION
      SINGLE
    }
  }

  Plan --> PlanType
  CustomerSubscription o-- Plan : planId

  namespace MonthlySubscriptionDetail集約 {
    class MonthlySubscriptionDetail {
      <<Aggregate Root>>
      id: MonthlySubscriptionDetailId
      customerSubscriptionId: CustomerSubscriptionId
      month: LocalDate
      selectedType: SelectedType
      status: ShippingStatus
      choices: List~CoffeeBeanId~
      shippingBeans: List~CoffeeBeanId~
    }

    class SelectedType {
      <<Enum>>
      omakase
      selfSelect
    }

    class ShippingStatus {
      <<Enum>>
      unshipped
      shipped
    }
  }

  MonthlySubscriptionDetail --> SelectedType
  MonthlySubscriptionDetail --> ShippingStatus
  MonthlySubscriptionDetail o-- CustomerSubscription : customerSubscriptionId

  namespace Shop集約 {
    class Shop {
      <<Aggregate Root>>
      id: ShopId
      shopifyShopId: ShopifyShopId
      name: String
      introduction: String?
      particular: String?
      shopUrl: String
      prefecture: Prefecture
      participationStatus: ParticipationStatus
      images: List~ShopImage~
    }

    class ShopImage {
      <<Entity>>
      id: ShopImageId
      type: ShopImageType
      imageUrl: ImageUrl
    }

    class ShopImageType {
      <<Enum>>
      main
      logo
    }

    class Prefecture {
      <<Enum>>
      HOKKAIDO
      ...
      OKINAWA
    }

    class ParticipationStatus {
      <<Enum>>
      BEFORE_PARTICIPATION
      PARTICIPATING
      DROPPED
    }
  }

  Shop *-- ShopImage
  Shop --> Prefecture
  Shop --> ParticipationStatus
  ShopImage --> ShopImageType

  namespace CoffeeBean集約 {
    class CoffeeBean {
      <<Aggregate Root>>
      id: CoffeeBeanId
      shopId: ShopId
      shopifyBeanId: ShopifyBeanId
      name: String
      description: String
      origin: String
      farm: String?
      roastLevel: RoastLevel
      processingMethod: ProcessingMethod
      isSpecialty: Boolean
      publishStatus: PublishStatus
      images: List~CoffeeBeanImage~
      tastes: List~CoffeeBeanTaste~
    }

    class CoffeeBeanImage {
      <<Entity>>
      id: CoffeeBeanImageId
      type: CoffeeBeanImageType
      imageUrl: ImageUrl
    }

    class CoffeeBeanImageType {
      <<Enum>>
      main
    }

    class CoffeeBeanTaste {
      <<Entity>>
      id: CoffeeBeanTasteId
      tasteId: TasteId
      evaluationValue: Int
    }

    class RoastLevel {
      <<Enum>>
      light
      medium
      city
      french
    }

    class ProcessingMethod {
      <<Enum>>
      fully_washed
      washed
      thermal_shock_natural
      natural
      wet_hulling
      honey
    }

    class PublishStatus {
      <<Enum>>
      DRAFT
      PUBLISHED
      INVALIDATED
    }
  }

  CoffeeBean --> RoastLevel
  CoffeeBean --> ProcessingMethod
  CoffeeBean --> PublishStatus
  CoffeeBean o-- Shop : shopId
  CoffeeBeanImage --> CoffeeBeanImageType
  CoffeeBean *-- CoffeeBeanImage
  CoffeeBean "1" *-- "1..*" CoffeeBeanTaste : 必須（全テイスト種別）

  namespace Taste集約 {
    class Taste {
      <<Aggregate Root>>
      id: TasteId
      name: String
    }
  }

  CoffeeBeanTaste o-- Taste : tasteId

  MonthlySubscriptionDetail o-- CoffeeBean : choices / shippingBeans
```

## 凡例

| 記号 | 意味 |
|------|------|
| `<<Aggregate Root>>` | 集約ルート。トランザクション整合性の境界 |
| `<<Entity>>` | 集約内のエンティティ。集約ルート経由でのみアクセス |
| `<<Value Object>>` | 値オブジェクト。不変で同値性で比較 |
| `<<Enum>>` | 列挙型。取りうる値が固定された値オブジェクト |
| `*--` (塗りつぶし菱形) | コンポジション。親が子のライフサイクルを管理 |
| `o--` (白抜き菱形) | ID参照。他の集約をIDで参照（直接保持しない） |
| `-->` | 依存。値オブジェクトの利用など |
