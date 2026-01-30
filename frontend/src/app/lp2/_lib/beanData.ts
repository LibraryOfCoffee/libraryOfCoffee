export interface TasteProfile {
  label: string;
  value: number;
}

export interface BeanDetail {
  /** 仮置きID（本番ではバックエンドのIDに置き換え予定） */
  id: string;
  imageSrc: string;
  tag: string;
  tagColor: string;
  name: string;
  description: string;
  detailDescription: string;
  origin: string;
  roastLevel: string;
  processing: string;
  roaster: string;
  tasteProfile: TasteProfile[];
}

export const beans: BeanDetail[] = [
  {
    id: "bean-001",
    imageSrc: "/lp2/bean1.png",
    tag: "人気No.1",
    tagColor: "#8B5A2B",
    name: "エチオピア イルガチェフェ",
    description: "フローラルな香りと柑橘系の酸味",
    detailDescription:
      "フローラルな香りと柑橘系の酸味が特徴。エチオピア南部のイルガチェフェ地区で栽培される、世界でも最高品質のアラビカ種です。標高1,800〜2,200mの高地で育まれたこの豆は、ジャスミンのような華やかなアロマとレモンやベルガモットを思わせる明るい酸味が際立ちます。口に含むと紅茶のようなクリーンな味わいが広がり、アフターテイストには甘いピーチの余韻が長く続きます。ウォッシュド精製により雑味のないクリアな風味に仕上がっており、ハンドドリップやフレンチプレスなど、豆の個性をダイレクトに楽しめる抽出方法がおすすめです。",
    origin: "エチオピア イルガチェフェ地区",
    roastLevel: "中煎り",
    processing: "ウォッシュド",
    roaster: "LIGHT UP COFFEE",
    tasteProfile: [
      { label: "酸味", value: 57 },
      { label: "苦味", value: 29 },
      { label: "コク", value: 34 },
      { label: "香り", value: 71 },
    ],
  },
  {
    id: "bean-002",
    imageSrc: "/lp2/bean2.png",
    tag: "定番",
    tagColor: "#7D9B76",
    name: "グアテマラ アンティグア",
    description: "チョコレートのような甘みとコク",
    detailDescription:
      "チョコレートのような甘みとしっかりとしたコクが特徴。グアテマラ・アンティグア地区の火山性土壌で育まれた、バランスの良い一杯です。アグア火山の麓、標高1,500m以上の農園で丁寧に手摘みされたこの豆は、ダークチョコレートやキャラメルを思わせるリッチな甘さと、ほのかなスパイス感が絶妙に調和しています。深煎りにすることでコクがさらに際立ち、ミルクとの相性も抜群。カフェラテやカプチーノにしても豆の風味がしっかりと感じられます。冷めてくるとブラウンシュガーのような甘い余韻に変化し、温度帯ごとに異なる表情を楽しめるのも魅力です。",
    origin: "グアテマラ アンティグア地区",
    roastLevel: "深煎り",
    processing: "フルウォッシュド",
    roaster: "丸山珈琲",
    tasteProfile: [
      { label: "酸味", value: 20 },
      { label: "苦味", value: 65 },
      { label: "コク", value: 70 },
      { label: "香り", value: 50 },
    ],
  },
  {
    id: "bean-003",
    imageSrc: "/lp2/bean3.png",
    tag: "おすすめ",
    tagColor: "#D4A574",
    name: "ケニア AA",
    description: "ベリー系の酸味と明るい後味",
    detailDescription:
      "ベリー系の鮮やかな酸味と明るい後味が魅力。ケニア中央高地で栽培されるAA等級の厳選豆をお届けします。ケニア独自のダブルファーメンテーション（二重発酵）製法により、カシスやブラックベリーを思わせる複雑なフルーティーさが引き出されています。浅煎りで仕上げることで、トマトのような鮮烈な酸味とグレープフルーツのような爽快感が際立ちます。ボディは中程度ながらジューシーで、冷めてもフルーツジュースのような甘酸っぱさを楽しめます。アイスコーヒーやコールドブリューにすると、よりフルーティーな個性が引き立つのでぜひお試しください。",
    origin: "ケニア 中央高地",
    roastLevel: "浅煎り",
    processing: "ケニア式ウォッシュド",
    roaster: "堀口珈琲",
    tasteProfile: [
      { label: "酸味", value: 75 },
      { label: "苦味", value: 15 },
      { label: "コク", value: 30 },
      { label: "香り", value: 60 },
    ],
  },
  {
    id: "bean-004",
    imageSrc: "/lp2/bean4.png",
    tag: "限定",
    tagColor: "#6B5B4D",
    name: "コロンビア スプレモ",
    description: "バランスの取れたマイルドな味わい",
    detailDescription:
      "バランスの取れたマイルドな味わいが特徴。コロンビアの高地で丁寧に栽培されたスプレモ等級の豆です。ウィラ県の標高1,700m付近の小規模農園で、シェードグロウン（日陰栽培）によりゆっくりと熟成された実だけを手摘み収穫しています。キャラメルのような甘みを軸に、青リンゴやオレンジピールのような穏やかな酸味がアクセントとなり、クリーミーなボディが全体をまとめ上げます。朝の一杯にも食後の一杯にも合う万能な味わいで、コーヒー初心者からベテランまで幅広く愛されるバランス型の一品です。",
    origin: "コロンビア ウィラ地区",
    roastLevel: "中煎り",
    processing: "ウォッシュド",
    roaster: "LIGHT UP COFFEE",
    tasteProfile: [
      { label: "酸味", value: 40 },
      { label: "苦味", value: 35 },
      { label: "コク", value: 55 },
      { label: "香り", value: 45 },
    ],
  },
  {
    id: "bean-005",
    imageSrc: "/lp2/bean5.png",
    tag: "定番",
    tagColor: "#8B5A2B",
    name: "ブラジル サントス",
    description: "ナッツのような香ばしさ",
    detailDescription:
      "ナッツのような香ばしさとまろやかな口当たりが特徴。ブラジルを代表するサントス港から出荷される高品質豆です。サンパウロ州の広大なセラード地域で機械収穫された後、天日乾燥（ナチュラル精製）で仕上げることで、ピーナッツやアーモンドを思わせる香ばしいアロマと、ミルクチョコレートのような甘みが凝縮されています。酸味が穏やかで苦味とのバランスが良く、どんな抽出方法でも安定した美味しさを発揮します。日常使いのコーヒーとして毎朝楽しめる、飽きのこない安心感のある味わいです。",
    origin: "ブラジル サンパウロ州",
    roastLevel: "中深煎り",
    processing: "ナチュラル",
    roaster: "丸山珈琲",
    tasteProfile: [
      { label: "酸味", value: 25 },
      { label: "苦味", value: 50 },
      { label: "コク", value: 60 },
      { label: "香り", value: 55 },
    ],
  },
  {
    id: "bean-006",
    imageSrc: "/lp2/bean6.png",
    tag: "定番",
    tagColor: "#D4A574",
    name: "マンデリン スマトラ",
    description: "深いコクとハーブの余韻",
    detailDescription:
      "深いコクとハーブのような独特の余韻が魅力。インドネシア・スマトラ島で伝統的な方法で精製された個性的な豆です。「スマトラ式」と呼ばれる独自の半水洗精製により、他の産地にはない重厚なアーシーさとシダーウッドのような木質系の香りが生まれます。口に含むとダークチョコレートやリコリスのような深い味わいが広がり、長い余韻の中にタバコリーフやスパイスのニュアンスが感じられます。深煎りとの相性が抜群で、エスプレッソのベースとしても存在感を発揮。個性派コーヒーを求める方にぜひ試していただきたい一品です。",
    origin: "インドネシア スマトラ島",
    roastLevel: "深煎り",
    processing: "スマトラ式",
    roaster: "堀口珈琲",
    tasteProfile: [
      { label: "酸味", value: 15 },
      { label: "苦味", value: 70 },
      { label: "コク", value: 80 },
      { label: "香り", value: 45 },
    ],
  },
  {
    id: "bean-007",
    imageSrc: "/lp2/bean7.png",
    tag: "季節限定",
    tagColor: "#7D9B76",
    name: "タンザニア キリマンジャロ",
    description: "柑橘系の爽やかな酸味",
    detailDescription:
      "柑橘系の爽やかな酸味とすっきりとした後味が特徴。キリマンジャロ山麓の豊かな土壌で育まれた上質な豆です。アフリカ最高峰キリマンジャロの北東斜面、標高1,400〜1,800mに位置する農園で栽培されており、昼夜の寒暖差が生み出すシャープでクリーンな酸味が持ち味です。オレンジやグレープフルーツのような柑橘系のフレーバーに加え、はちみつのようなほのかな甘みが後味に残ります。中煎りで仕上げることで果実味と甘みのバランスが最も引き立ち、ブラックで飲むのがおすすめ。季節限定のため、この機会をお見逃しなく。",
    origin: "タンザニア キリマンジャロ地区",
    roastLevel: "中煎り",
    processing: "ウォッシュド",
    roaster: "LIGHT UP COFFEE",
    tasteProfile: [
      { label: "酸味", value: 65 },
      { label: "苦味", value: 20 },
      { label: "コク", value: 35 },
      { label: "香り", value: 60 },
    ],
  },
  {
    id: "bean-008",
    imageSrc: "/lp2/bean8.png",
    tag: "人気",
    tagColor: "#8B5A2B",
    name: "コスタリカ タラス",
    description: "はちみつのような甘みと透明感",
    detailDescription:
      "はちみつのような甘みと透明感のある味わいが特徴。コスタリカ・タラス地区の高標高で栽培された希少な豆です。ハニープロセスと呼ばれる精製方法で、コーヒーチェリーの果肉を一部残したまま乾燥させることで、蜂蜜のようなとろりとした甘みとフローラルな香りが豆に染み込んでいます。口当たりはシルキーで、マスカットや白桃のようなジューシーなフルーツ感と、ジャスミンティーを思わせる上品なアロマが特徴的。酸味は穏やかで、全体的にエレガントな印象の一杯に仕上がっています。ペーパードリップでじっくり淹れると、甘みと透明感が最も引き立ちます。",
    origin: "コスタリカ タラス地区",
    roastLevel: "中煎り",
    processing: "ハニープロセス",
    roaster: "丸山珈琲",
    tasteProfile: [
      { label: "酸味", value: 45 },
      { label: "苦味", value: 25 },
      { label: "コク", value: 50 },
      { label: "香り", value: 70 },
    ],
  },
];

export function getBeanById(id: string): BeanDetail | undefined {
  return beans.find((b) => b.id === id);
}
