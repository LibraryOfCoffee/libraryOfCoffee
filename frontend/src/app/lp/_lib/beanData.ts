export interface TasteProfile {
  label: string;
  value: number;
}

export interface BeanDetail {
  id: string;
  imageSrc: string;
  tag: string;
  tagColor: string;
  name: string;
  subName: string;
  description: string;
  detailDescription: string;
  origin: string;
  farm: string;
  roastLevel: string;
  processing: string;
  roaster: string;
  roasterLink: string;
  tasteProfile: TasteProfile[];
}

export const beans: BeanDetail[] = [
  {
    id: "49928992751849",
    imageSrc: "/beanImages/49928992751849.jpg",
    tag: "中煎り",
    tagColor: "#A6683D",
    name: "東ティモール",
    subName: "東ティモール・ロダン集落",
    description: "レモンやみかんの様な酸味とヘーゼルナッツを思わせる",
    detailDescription:
      "・レモンやみかんの様な酸味とヘーゼルナッツを思わせる風味。\n・サトウキビらしい砂糖の穏やかな甘味も感じさせ、デイリーのコーヒーに適したテイスト。\n\n【どんなコーヒー？】\n19世紀に当時の東ティモールを統治していたポルトガルが、高地の気候を活かして栽培を始めたのが起源です。現在は小規模農家が中心となって生産され、熟した果実のようなやさしい甘さが特徴。世界的なスペシャルティコーヒー人気を背景に、丁寧な精製と品質重視の豆として注目されています。",
    origin: "東ティモール, エルメラ県, レテフォホ郡",
    farm: "ロダン集落",
    roastLevel: "中煎り",
    processing: "Fully Washed",
    roaster: "LUSH COFFEE",
    roasterLink: "https://lush-coffee.com/",
    tasteProfile: [
      { label: "酸味", value: 60 },
      { label: "苦味", value: 20 },
      { label: "甘み", value: 80 },
      { label: "コク", value: 60 },
      { label: "香り", value: 80 },
    ],
  },
  {
    id: "49928994849001",
    imageSrc: "/beanImages/49928994849001.jpg",
    tag: "中深煎り",
    tagColor: "#5C3317",
    name: "東ティモール",
    subName: "東ティモール・ロダン集落",
    description: "キャラメル、プラムの甘さ、くるみの香ばしさ",
    detailDescription:
      "・キャラメル、プラムの甘さ、くるみの香ばしさ\n・グレープフルーツのようなハーブと柑橘系の風味を楽しめるコーヒーです\n\n【どんなコーヒー？】\n19世紀に当時の東ティモールを統治していたポルトガルが、高地の気候を活かして栽培を始めたのが起源です。現在は小規模農家が中心となって生産され、熟した果実のようなやさしい甘さが特徴。世界的なスペシャルティコーヒー人気を背景に、丁寧な精製と品質重視の豆として注目されています。",
    origin: "東ティモール, エルメラ県, レテフォホ郡",
    farm: "ロダン集落",
    roastLevel: "中深煎り",
    processing: "Fully Washed",
    roaster: "LUSH COFFEE",
    roasterLink: "https://lush-coffee.com/",
    tasteProfile: [
      { label: "酸味", value: 40 },
      { label: "苦味", value: 80 },
      { label: "甘み", value: 60 },
      { label: "コク", value: 80 },
      { label: "香り", value: 80 },
    ],
  },
  {
    id: "49929671770345",
    imageSrc: "/beanImages/49929671770345.jpg",
    tag: "中深煎り",
    tagColor: "#5C3317",
    name: "グアテマラ",
    subName: "グアテマラ・コンポステラ",
    description: "ハイカカオのチョコレートのような酸と、ほどよい苦味",
    detailDescription:
      "ハイカカオのチョコレートのような酸と、ほどよい苦味が特徴です。\n\n【どんなコーヒー？】\nグアテマラは中米に位置し、栄養豊富な火山性土壌と高地の気候に恵まれたコーヒー産地です。標高の高いエリアでは寒暖差が大きく、実がゆっくり成熟することで豆が引き締まり、風味の良いコーヒーが育ちやすいのが特徴。その中でも標高1,500m以上の高地で育つ豆は、SHB（Strictly Hard Bean）と呼ばれる最上位クラスに分類されます。こうした貴重なSHB豆の中から厳選されたロットに、「コンポステラ（星の野原）」という特別な名前が付けられました。",
    origin: "ウエウエテナンゴ",
    farm: "ラ ボルサ農園",
    roastLevel: "中深煎り",
    processing: "Washed",
    roaster: "NORTH NODE COFFEE",
    roasterLink: "https://northnode.base.shop/",
    tasteProfile: [
      { label: "酸味", value: 40 },
      { label: "苦味", value: 80 },
      { label: "甘み", value: 60 },
      { label: "コク", value: 80 },
      { label: "香り", value: 60 },
    ],
  },
  {
    id: "49929740910825",
    imageSrc: "/beanImages/49929740910825.jpg",
    tag: "深煎り",
    tagColor: "#321E14",
    name: "インディア",
    subName: "インディア・APAA",
    description: "ナッツ系の香ばしさとペッパーのようなキレのある爽やかさ",
    detailDescription:
      "ナッツ系の香ばしさの中にペッパーのようなキレのある爽やかさが特徴です。\n\n【どんなコーヒー？】\nインドは南部を中心に高原地帯が広がり、穏やかな気候の中でじっくり成熟するため、角の取れた飲み口になりやすいのが魅力。APAAは「Arabica Plantation AA」の略で、サイズや形、が基準を満たし、異物混入のない高品質なアラビカ種（washed製法）であることを保証。インドコーヒーの特徴である独特なスパイスのような香りが特徴です。",
    origin: "シェバロイ地区",
    farm: "ジュリアンピーク農園",
    roastLevel: "深煎り",
    processing: "Washed",
    roaster: "NORTH NODE COFFEE",
    roasterLink: "https://northnode.base.shop/",
    tasteProfile: [
      { label: "酸味", value: 20 },
      { label: "苦味", value: 100 },
      { label: "甘み", value: 40 },
      { label: "コク", value: 80 },
      { label: "香り", value: 60 },
    ],
  },
  {
    id: "49929754771689",
    imageSrc: "/beanImages/49929754771689.jpg",
    tag: "中深煎り",
    tagColor: "#5C3317",
    name: "ルワンダ",
    subName: "ルワンダ・シーラCWS",
    description:
      "バレンシアオレンジや八朔を思わせるフレッシュで香り高い柑橘の酸と三温糖の上品な甘さ",
    detailDescription:
      'バレンシアオレンジや八朔を思わせるフレッシュで香り高い柑橘の酸の印象と三温糖の上品な甘さが特徴です。\n\n【どんなコーヒー？】\nルワンダは東アフリカ内陸の高原国で、"千の丘の国"と呼ばれるほど起伏の多い地形が特徴。2000年代以降、政府主導でCWS（Coffee Washing Station）の整備と品質改善が進み、スペシャルティコーヒー産地として注目されるようになりました。\n\n西部のニャビフ郡にあるシーラCWSは、標高1,850〜2,300mという国内最高峰クラスの環境に位置。発酵・比重選別・陰干しと天日乾燥を重ねたフルウォッシュドで丁寧に精製しています。こうした高地環境と品質重視の取り組みにより、柑橘の明るい酸と上品な甘さが引き出されます。',
    origin: "西部州ニャビフ群",
    farm: "",
    roastLevel: "中煎り",
    processing: "Washed",
    roaster: "Tama Coffee Roaster",
    roasterLink: "https://www.tamacoffeeroaster.com/",
    tasteProfile: [
      { label: "酸味", value: 80 },
      { label: "苦味", value: 80 },
      { label: "甘み", value: 60 },
      { label: "コク", value: 80 },
      { label: "香り", value: 80 },
    ],
  },
  {
    id: "49929756672233",
    imageSrc: "/beanImages/49929756672233.jpg",
    tag: "中深煎り",
    tagColor: "#5C3317",
    name: "ホンジュラス",
    subName: "ホンジュラス・エルピノ農園",
    description:
      "シルクのような飲み口に、オレンジやプラムなど華やかな風味と心地よい甘さ",
    detailDescription:
      "シルクのような飲み口に、オレンジやプラムなど華やかな風味。滑らかな質感と心地よい甘さが特徴です。\n\n【どんなコーヒー？】\nホンジュラスは中米の山岳国で、18〜19世紀にコーヒー栽培が始まり、20世紀に入って輸出産業として本格化しました。近年は品質改善と市場開拓が進み、中米最大級のコーヒー輸出国として成長。国際品評会Cup of Excellence（COE）でも入賞ロットが高く評価され、スペシャルティ産地としての認知が高まっています。\n\n今回の豆は、高地（標高約1,800m）で育つティピカ種です。この地域は小規模農家が中心で、冷涼な環境の中でチェリーがゆっくり成熟するため、クリーンでバランスの良い風味が生まれやすいのが特徴です。",
    origin: "インディブカ県マサグアラ",
    farm: "エル・ピノ農園",
    roastLevel: "中深煎り",
    processing: "Washed",
    roaster: "Tama Coffee Roaster",
    roasterLink: "https://www.tamacoffeeroaster.com/",
    tasteProfile: [
      { label: "酸味", value: 60 },
      { label: "苦味", value: 80 },
      { label: "甘み", value: 60 },
      { label: "コク", value: 80 },
      { label: "香り", value: 60 },
    ],
  },
  {
    id: "49918863933673",
    imageSrc: "/beanImages/49918863933673.jpg",
    tag: "浅煎り",
    tagColor: "#E2B36E",
    name: "コロンビア",
    subName: "コロンビア・サーマルショックナチュラル",
    description:
      "ブルーベリーの爆発的なフレーバーとヨーグルトのような酸味、チョコレートの余韻",
    detailDescription:
      "・ブルーベリーの爆発的なフレーバー、後からヨーグルトのような酸味。\n・最後にはチョコレートで締めくくってくれる豆です。\n\n【どんなコーヒー？】\n発酵工程の途中で意図的に温度差を与える「サーマルショック・ナチュラル」という精製方法を採用。発酵の動きをコントロールすることで、果実由来の香りや甘さを豆の内部にしっかりと閉じ込め、フルーティーな風味をより明確に引き出しています。\n\n品種はスーダンルメ。アフリカ由来の希少品種で、病害に強く、華やかな果実感と透明感のある甘さが出やすいことで知られています。近年のコロンビアでは、高品質かつ安定した生産が可能な品種として栽培が広がっており、浅煎りに仕上げることで、スーダンルメ本来のクリーンで明るい個性を際立たせています。",
    origin: "コロンビア",
    farm: "ブエノスアイレス農園",
    roastLevel: "浅煎り",
    processing: "サーマルショックナチュラル",
    roaster: "MOSHIMOSHI COFFEE",
    roasterLink: "https://moshimoshi.buyshop.jp/",
    tasteProfile: [
      { label: "酸味", value: 80 },
      { label: "苦味", value: 20 },
      { label: "甘み", value: 60 },
      { label: "コク", value: 40 },
      { label: "香り", value: 100 },
    ],
  },
  {
    id: "49929003139305",
    imageSrc: "/beanImages/49929003139305.jpg",
    tag: "中煎り",
    tagColor: "#A6683D",
    name: "インドネシア",
    subName: "インドネシア・マンデリン/ビンタンリマ",
    description:
      "オレンジピールやグレープフルーツの明るい風味からカカオのほろ苦さまで",
    detailDescription:
      "・オレンジピールやグレープフルーツの明るい風味\n・果物のフレーバーから徐々にカカオのほろ苦さまで楽しめる珈琲です！\n\n【どんなコーヒー？】\nインドネシア・スマトラ島北部は、火山性土壌と多雨な高地環境に恵まれ、力強く個性的なコーヒーが育つ産地です。この地を代表するマンデリンは、重厚なコクと丸みのある口当たりが特徴。現地特有のスマトラ式（ウェットハル）精製によって、深みのある風味が生まれます。\n\nビンタンリマは、インドネシア語で「五つ星」を意味する品質呼称。特定の農園名ではなく、複数の小規模農家の中から完熟チェリーのみを厳選し、欠点豆の少なさやカップ品質といった基準を満たしたロットに与えられる名称です。マンデリンの中でも安定したクオリティが評価され、日本でも定番として親しまれています。",
    origin: "インドネシア スマトラ島北部",
    farm: "ビンタンリマの小農家さん",
    roastLevel: "中煎り",
    processing: "スマトラ式",
    roaster: "MOSHIMOSHI COFFEE",
    roasterLink: "https://moshimoshi.buyshop.jp/",
    tasteProfile: [
      { label: "酸味", value: 60 },
      { label: "苦味", value: 60 },
      { label: "甘み", value: 60 },
      { label: "コク", value: 60 },
      { label: "香り", value: 80 },
    ],
  },
];

export function getBeanById(id: string): BeanDetail | undefined {
  return beans.find((b) => b.id === id);
}
