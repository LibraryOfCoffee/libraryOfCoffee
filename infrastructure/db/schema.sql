CREATE TABLE IF NOT EXISTS administrators (
    id              CHAR(36)     NOT NULL PRIMARY KEY                          COMMENT '管理者ID',
    email           VARCHAR(255) NOT NULL UNIQUE                               COMMENT 'メールアドレス',
    hashed_password VARCHAR(255) NOT NULL                                      COMMENT 'ハッシュ化されたパスワード',
    role            ENUM('admin', 'staff') NOT NULL                            COMMENT 'ロール',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理者テーブル';

CREATE TABLE IF NOT EXISTS customers (
    id                  CHAR(36)     NOT NULL PRIMARY KEY                      COMMENT '顧客ID',
    shopify_customer_id VARCHAR(255) NOT NULL UNIQUE                           COMMENT 'ShopifyのカスタマーID',
    status              ENUM('active', 'withdrawn') NOT NULL                   COMMENT 'ステータス',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='顧客テーブル';

CREATE TABLE IF NOT EXISTS plans (
    id              CHAR(36)                        NOT NULL PRIMARY KEY       COMMENT 'プランID',
    shopify_plan_id VARCHAR(255)                    NOT NULL UNIQUE            COMMENT 'ShopifyのプランID',
    label           VARCHAR(50)                     NOT NULL                   COMMENT 'プラン表示名（例: はじめて）',
    gram_weight     INT                             NOT NULL                   COMMENT '1種あたりのグラム数（30/60/90）',
    bean_quantity   INT                             NOT NULL                   COMMENT '豆の種類数（3/4/5）',
    price           INT                             NOT NULL                   COMMENT '価格',
    type            ENUM('SUBSCRIPTION', 'SINGLE')  NOT NULL                   COMMENT 'プラン種別',
    is_recommended  BOOLEAN                         NOT NULL DEFAULT FALSE     COMMENT 'おすすめバッジ',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='プランマスタテーブル';

CREATE TABLE IF NOT EXISTS shops (
    id              CHAR(36)      NOT NULL PRIMARY KEY                         COMMENT '店舗ID',
    shopify_shop_id VARCHAR(255)  NOT NULL UNIQUE                              COMMENT 'ShopifyのショップID',
    name            VARCHAR(255)  NOT NULL                                     COMMENT '店舗名',
    introduction    TEXT                                                       COMMENT '店舗紹介',
    particular      TEXT                                                       COMMENT 'こだわり',
    shop_url        VARCHAR(2048)  NOT NULL                                     COMMENT '店舗URL',
    prefecture      VARCHAR(255)  NOT NULL                                       COMMENT '都道府県',
    participation_status ENUM('BEFORE_PARTICIPATION', 'PARTICIPATING', 'DROPPED') NOT NULL DEFAULT 'BEFORE_PARTICIPATION' COMMENT '参画ステータス',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店舗マスタテーブル';

CREATE TABLE IF NOT EXISTS tastes (
    id   CHAR(36)     NOT NULL PRIMARY KEY                                     COMMENT 'テイストID',
    name VARCHAR(255) NOT NULL                                                 COMMENT 'テイスト名',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='テイストマスタテーブル';

CREATE TABLE IF NOT EXISTS customer_subscriptions (
    id              CHAR(36) NOT NULL PRIMARY KEY                              COMMENT '契約ID',
    customer_id     CHAR(36) NOT NULL                                          COMMENT '顧客ID',
    plan_id         CHAR(36) NOT NULL                                          COMMENT 'プランID',
    status          ENUM('active', 'canceled', 'ban') NOT NULL                COMMENT '契約ステータス',
    contracted_from DATE     NOT NULL                                          COMMENT '契約開始日',
    contracted_to   DATE                                                       COMMENT '契約終了日（解約前はNULL）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    FOREIGN KEY (customer_id) REFERENCES customers (id),
    FOREIGN KEY (plan_id) REFERENCES plans (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='顧客のサブスクリプション契約テーブル';

CREATE TABLE IF NOT EXISTS coffee_beans (
    id                CHAR(36)      NOT NULL PRIMARY KEY                       COMMENT '珈琲豆ID',
    shop_id           CHAR(36)      NOT NULL                                   COMMENT '店舗ID',
    shopify_bean_id   VARCHAR(255)  NOT NULL UNIQUE                            COMMENT 'Shopifyの珈琲豆ID',
    name              VARCHAR(255)  NOT NULL                                   COMMENT '珈琲豆名',
    description       TEXT          NOT NULL                                     COMMENT '説明',
    origin            VARCHAR(255)  NOT NULL                                   COMMENT '産地',
    farm              VARCHAR(255)                                             COMMENT '農園',
    roast_level       ENUM('LIGHT', 'CINNAMON', 'MEDIUM', 'CITY', 'FRENCH') NOT NULL COMMENT '焙煎度',
    processing_method ENUM('FULLY_WASHED', 'WASHED', 'ANAEROBIC_WASHED', 'THERMAL_SHOCK_NATURAL', 'NATURAL', 'ANAEROBIC_NATURAL', 'DRY_ON_TREE_NATURAL', 'LACTIC_NATURAL', 'WET_HULLING', 'HONEY', 'MOUNTAIN_WATER', 'LADO_A_LADO_PROCESS', 'LADO_A_LADO_PROCESS_FULLY_WASHED') NOT NULL COMMENT '精製方法（LADO_A_LADO_PROCESS_FULLY_WASHEDはブレンドコーヒー向けの一時的な複合種別）',
    is_specialty      BOOLEAN       NOT NULL DEFAULT FALSE                     COMMENT 'スペシャルティコーヒーかどうか',
    publish_status    ENUM('DRAFT', 'PUBLISHED', 'INVALIDATED') NOT NULL DEFAULT 'DRAFT' COMMENT '公開状態',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    FOREIGN KEY (shop_id) REFERENCES shops (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='珈琲豆のマスタテーブル';

CREATE TABLE IF NOT EXISTS monthly_subscription_details (
    id                       CHAR(36) NOT NULL PRIMARY KEY                     COMMENT '詳細ID',
    customer_subscription_id CHAR(36) NOT NULL                                 COMMENT '契約ID',
    month                    DATE     NOT NULL                                 COMMENT '対象年月',
    selected_type            ENUM('omakase', 'self_select') NOT NULL           COMMENT '選択タイプ（おまかせ／自己選択）',
    status                   ENUM('unshipped', 'shipped')   NOT NULL           COMMENT '発送ステータス',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    UNIQUE (customer_subscription_id, month),
    FOREIGN KEY (customer_subscription_id) REFERENCES customer_subscriptions (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='月次サブスクリプション詳細テーブル';

CREATE TABLE IF NOT EXISTS coffee_bean_images (
    id            CHAR(36)      NOT NULL PRIMARY KEY                           COMMENT '画像ID',
    coffee_bean_id CHAR(36)     NOT NULL                                       COMMENT '珈琲豆ID',
    type          ENUM('MAIN') NOT NULL                                        COMMENT '画像種別',
    image_url     VARCHAR(2048) NOT NULL                                       COMMENT '画像URL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    FOREIGN KEY (coffee_bean_id) REFERENCES coffee_beans (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='珈琲豆の画像テーブル';

CREATE TABLE IF NOT EXISTS coffee_bean_tastes (
    id               CHAR(36) NOT NULL PRIMARY KEY                             COMMENT '評価ID',
    coffee_bean_id   CHAR(36) NOT NULL                                         COMMENT '珈琲豆ID',
    tastes_id        CHAR(36) NOT NULL                                         COMMENT 'テイストID',
    evaluation_value INT      NOT NULL                                         COMMENT '評価値',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    UNIQUE (coffee_bean_id, tastes_id),
    FOREIGN KEY (coffee_bean_id) REFERENCES coffee_beans (id),
    FOREIGN KEY (tastes_id) REFERENCES tastes (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='珈琲豆のテイスト評価テーブル';

CREATE TABLE IF NOT EXISTS shop_images (
    id        CHAR(36)      NOT NULL PRIMARY KEY                               COMMENT '画像ID',
    shop_id   CHAR(36)      NOT NULL                                           COMMENT '店舗ID',
    type      ENUM('MAIN', 'LOGO') NOT NULL                                    COMMENT '画像種別',
    image_url VARCHAR(2048) NOT NULL                                           COMMENT '画像URL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    FOREIGN KEY (shop_id) REFERENCES shops (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店舗の画像テーブル';

CREATE TABLE IF NOT EXISTS customer_coffee_bean_choices (
    id                             CHAR(36) NOT NULL PRIMARY KEY               COMMENT '選択ID',
    monthly_subscription_detail_id CHAR(36) NOT NULL                           COMMENT '月次詳細ID',
    coffee_bean_id                 CHAR(36) NOT NULL                           COMMENT '珈琲豆ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    UNIQUE (monthly_subscription_detail_id, coffee_bean_id),
    FOREIGN KEY (monthly_subscription_detail_id) REFERENCES monthly_subscription_details (id),
    FOREIGN KEY (coffee_bean_id) REFERENCES coffee_beans (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='顧客が選択した珈琲豆テーブル';

CREATE TABLE IF NOT EXISTS actual_shipping_coffee_beans (
    id                             CHAR(36) NOT NULL PRIMARY KEY               COMMENT '発送豆ID',
    monthly_subscription_detail_id CHAR(36) NOT NULL                           COMMENT '月次詳細ID',
    coffee_bean_id                 CHAR(36) NOT NULL                           COMMENT '珈琲豆ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
    UNIQUE (monthly_subscription_detail_id, coffee_bean_id),
    FOREIGN KEY (monthly_subscription_detail_id) REFERENCES monthly_subscription_details (id),
    FOREIGN KEY (coffee_bean_id) REFERENCES coffee_beans (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='実際に発送する珈琲豆テーブル';

CREATE TABLE IF NOT EXISTS api_clients (
    id               CHAR(36)       NOT NULL PRIMARY KEY                          COMMENT 'APIクライアントID(UUID)',
    client_id        VARCHAR(255)   NOT NULL UNIQUE                               COMMENT '識別子(X-Client-Idと一致, 例 ssr-frontend)',
    description      VARCHAR(255)   NOT NULL                                      COMMENT '用途説明',
    encrypted_secret VARBINARY(512) NOT NULL                                      COMMENT 'AES-GCMで封筒暗号化したHMAC秘密鍵(iv||ciphertext||tag)',
    is_active        BOOLEAN        NOT NULL DEFAULT TRUE                          COMMENT '有効フラグ(失効はfalse)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP                             COMMENT '作成日時',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='APIクライアント(HMAC)鍵管理テーブル';
