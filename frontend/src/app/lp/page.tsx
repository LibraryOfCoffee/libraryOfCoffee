import Header from './_components/Header/header';
import FeatureCard from './_components/FeatureCard/featureCard';
import Section from './_components/Section/section';
import UserVoiceCard from './_components/UserVoiceCard/userVoiceCard';
import { FaBeer } from 'react-icons/fa';
import './lp.css';


const featureList = [
  {
    number: "01",
    title: "気軽に選ぶ",
    iconElement: <FaBeer size={50} />,
    subtitle: "毎月変わる10店舗のリストから気になる珈琲を選択",
    details: [
      '1種類当たり30gで気軽にチャレンジ。',
      'どの珈琲豆を選んでも、定額1,500円',
      '普段行けない地元で愛される店舗'
    ]
  },
  {
    number: "02",
    title: "飲んで試せる",
    iconElement: <FaBeer size={50} />,
    subtitle: "気になる豆2種類とおすすめ1種類の合計3種類が届く",
    details: [
      '自分の気になる豆を体験',
      '未知なる珈琲豆との出会い',
      '注文後の焙煎なので新鮮な豆が届く'
    ]
  },
  {
    number: "03",
    title: "知識・体験が広がる",
    iconElement: <FaBeer size={50} />,
    subtitle: "店舗のこだわりや珈琲豆の特徴を分かりやすく紹介",
    details: [
      '店舗のこだわりがわかる',
      '豆の産地や焙煎などの背景を知れる',
      '店舗おすすめの入れ方が体験可能'
    ]
  },
]

export default function LP() {
  return (
    <>
      <Header />
      <div className="lp-container">
        {/* Hero Section */}
        <section className="lp-hero">
          <div className="lp-hero-container">
            <div className="lp-hero-inner">
              {/* Background Image Placeholder */}
              <div className="lp-hero-background">
                <div className="lp-hero-gradient"></div>
              </div>

              <div className="lp-hero-content">
                {/* FIXME: img部分仮置き */}
                <div className='lp-hero-main-image' style={{ backgroundColor: 'yellow' }}>
                  <img height={380} alt="lp-hero-gradient"></img>
                </div>
                <h1 className="lp-hero-title">
                  色んな珈琲と出会える豆図書
                </h1>
                <p className="lp-hero-subtitle">
                  高品質なコーヒー豆を30gで気軽にお試し。<br />
                  自分好みのコーヒー豆を見つけたい・美味しい入れ方を探求したいあなたに。
                </p>
                <button className="lp-button-large">
                  XXXXXを購入する
                </button>
              </div>
            </div>
          </div>
        </section>
        {/* Features Section */}
        <Section
          title={'豆図書のメリット'}
          subtitle={`美味しい珈琲豆って何だろう？珈琲豆を気軽に選び・自宅で飲んでみて・知識や体験を広げよう！`}
        >
          <div className="lp-features-grid">
            {
              featureList.map((feature, index) =>
                <FeatureCard
                  key={index}
                  number={feature.number}
                  mainTitle={feature.title}
                  iconElement={feature.iconElement}
                  heading={feature.subtitle}
                  features={feature.details}
                />
              )
            }
          </div>
        </Section>

        <Section
          title={'色んな珈琲豆と出会える場所。豆図書'}
          subtitle={'利用者の声'}
        >
          <div className="lp-testimonials-grid">
            <UserVoiceCard
              imageUrl="/path/to/image.jpg"
              userName="R.Oさん"
              userAge="20代"
              userGender="男性"
              tags={["社会人", "複数種類を少量で体験", "全国のコーヒ店舗を選択可能"]}
              qaList={[
                {
                  question: "サービスを利用してみて、いかがでしたか？",
                  answers: [
                    "量がちょうどいいです。",
                    "1種類30gという少量だから、家に豆が溜まりすぎることもなく、鮮度を保ったまま飲みきれる。",
                    "以前はサブスクを続けているうちに届く量が多く、豆が余ってしまうことが多かったのですが、このサービスは自分のペースにピッタリ合っていて、無理なく続けられています。"
                  ]
                },
                {
                  question: "サービスの使いやすさについては？",
                  answers: [
                    "バリエーションが豊富で、選びやすいのが魅力です。",
                    "過去に北海道や石川など色んな地域に住んでいたのですが、さまざまな地域のお店の豆を選んで注文できるのが嬉しいですね。全国を旅するように、懐かしいお店や知らない焙煎所の味を自宅で楽しめるのは特別な体験です。",
                  ]
                },
                {
                  question: "情報面で良かった点はありますか？",
                  answers: [
                    "豆やお店の紹介メッセージがわかりやすくて、読むのが楽しみでした。",
                    "豆を飲む前に背景を知ることで、味わい方が変わるんですよね。"
                  ]
                },
                {
                  question: "パッケージや届き方については？",
                  answers: [
                    "シンプルで無駄がなく、開けた瞬間から好印象でした。",
                    "ポスト投函でも豆の状態はしっかり保たれていて、袋も嵩張らない。30gずつの小分けなので鮮度を気にせず使えるし、捨てる時も手軽でいいですね。自分の\"溜め込みがち\"な性格にも合っていて、気軽にコーヒーを楽しめています。"
                  ]
                }
              ]}
            />
            <UserVoiceCard
              imageUrl="/path/to/image.jpg"
              userName="T.Fさん"
              userAge="50代"
              userGender="女性"
              tags={["社会人", "色んな珈琲豆を体験", "店舗の味を自宅で再現"]}
              qaList={[
                {
                  question: "サービスを利用してみて、いかがでしたか？",
                  answers: [
                    "全国の多くのお店を実際に飲み歩くのは物理的に難しいですし、どこで何が手に入るかもわかりづらい。そんな中で、信頼できる基準で選ばれた豆や店舗を紹介してもらい、体験できるのはとても良いです。",
                  ]
                },
                {
                  question: "さまざまなコーヒーを試してみたいと思われた背景を教えてください。",
                  answers: [
                    "単純に「もっといろいろなコーヒーを知りたい」という気持ちからでした。豆そのものの違いも面白いし、焙煎やお店ごとの個性を感じるのも楽しい。豆と店舗、どちらが大事かと言われると、やっぱり「どちらも」だと思います。",
                  ]
                },
                {
                  question: "今回、珈琲の探求は進みましたか？",
                  answers: [
                    "進みました。店舗レシピをベースに、温度・時間・粉量などを変えて「狙って味を動かす」感覚がつきました。知見としては店舗情報というより、自分の抽出体験が深まった点が良かったです。",
                  ]
                },
                {
                  question: "目指しているゴールはありますか？",
                  answers: [
                    "明確な到達点は決めていません。まずはいろいろな淹れ方を試し、体験を広げたい。基準が一つできただけでも十分に嬉しいです。",
                  ]
                }
              ]}
            />
            <UserVoiceCard
              imageUrl="/path/to/image.jpg"
              userName="R.Oさん"
              userAge="20代"
              userGender="女性"
              tags={["学生", "豆の情報をディグル", "好みの1杯を探索"]}
              qaList={[
                {
                  question: "サービスを利用してみて、いかがでしたか？",
                  answers: [
                    "定期的に美味しい豆が届くのは、やっぱり嬉しいですね。",
                    "わざわざ買いに行かなくても、自宅で新しい豆に出会えるのはとても便利で、気づけばそれが生活の一部になっていました。届いた袋を開けて香りを確かめる瞬間が楽しみで、正直もうやめづらいくらいです。",
                  ]
                },
                {
                  question: "始めたきっかけは？",
                  answers: [
                    "「もっと美味しいコーヒーに出会いたい」という期待からでした。",
                    "いろいろな豆を飲み比べていくうちに、酸味や香り香り、コクなど、自分がどんな味を好むのかが少しずつ分かってきて、飲むたびに\"発見\"があるのが面白いんです。",
                  ]
                },
                {
                  question: "印象に残っている豆はありますか？",
                  answers: [
                    "パプアニューギニアの豆に出会ったときは衝撃でした。",
                    "それまでのイメージを覆すような香りと味で、「こんなコーヒーがあるんだ」と思いましたね。そこから、\"自分にとっての一杯\"を探すのが楽しくなりました。器や道具を選ぶように、「これが自分の豆」と言える存在を見つけたいと思っています。",
                  ]
                },
                {
                  question: "理想のコーヒー体験とは？",
                  answers: [
                    "最終的には、自分にとっての\"これ\"という一種類に辿り着くこと。",
                    "毎朝その豆で淹れた一杯を飲んで、「やっぱりこれだな」と感じられる瞬間が一番幸せです。",
                  ]
                }
              ]}
            />
          </div>
        </Section>

        <Section
          title={'利用料'}
          subtitle={'料金プラン'}
        >
          <div className="lp-pricing-grid">
            {/* Free Plan */}
            <div className="lp-pricing-card">
              <h3 className="lp-pricing-title">スタンダードプラン</h3>
              <div className="lp-pricing-price">
                <span className="lp-pricing-amount">0円</span>
                <span className="lp-pricing-period">/月</span>
              </div>
              <ul className="lp-pricing-features">
                <li className="lp-pricing-feature">
                  <span className="lp-pricing-feature-icon">✓</span>
                  <span className="lp-pricing-feature-text">初期費用0円</span>
                </li>
                <li className="lp-pricing-feature">
                  <span className="lp-pricing-feature-icon">✓</span>
                  <span className="lp-pricing-feature-text">月額費用0円</span>
                </li>
                <li className="lp-pricing-feature">
                  <span className="lp-pricing-feature-icon">✓</span>
                  <span className="lp-pricing-feature-text">決済手数料 3.6%+40円</span>
                </li>
              </ul>
              <button className="lp-button-secondary">
                詳しく見る
              </button>
            </div>
          </div>
        </Section>
      </div >
    </>
  );
}
