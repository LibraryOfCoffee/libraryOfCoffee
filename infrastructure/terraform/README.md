## 事前準備(初回限定)
terraform-state保存用のS3を手で作成する。
※2025/12/29 ysato作成済み

```:shell
AWS_PROFILE=coffee aws s3api create-bucket --bucket mametosho-terraform-state-dev --create-bucket-configuration LocationConstraint=ap-northeast-1
```

```:shell
AWS_PROFILE=coffee terraform destroy
```


## backend
### localでの起動コマンド
```:shell
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```
