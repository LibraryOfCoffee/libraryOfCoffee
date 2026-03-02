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
    id: "50466550153449",
    imageSrc: "/beanImages/50466550153449.jpg",
    tag: "浅煎り",
    tagColor: "#E2B36E",
    name: "コスタリカ",
    subName: "コスタリカ シュマバ シドラ レッドハニー",
    description:
      "真っ赤な林檎のような甘さのある香りと丸く柔らかく柔らかい口当たり",
    detailDescription:
      "モンテジャノボニート農園はウエストバレーに位置する高品質志向の農園。収穫したチェリーは、生産者自らが運営するシュマバ・マイクロミルで一貫して加工・精製され、熟度管理と発酵設計の徹底により、クリーンで立体的な風味を引き出しています。品種はシドラ。エクアドルで発見された、レッドブルボンとティピカの自然交配といわれる希少種で、名の通り（シードル＝リンゴ酒）華やかな果実香と鮮やかな酸、シルクのような質感が特徴です。\nレッドハニー精製により、果肉の粘液質を残して乾燥させることで、ナチュラルのような甘さとウォッシュドのようなクリーンさを両立。レモンや青りんごを思わせる明るい酸とフローラルな甘さが重なり、透明感のある余韻が続きます。",
    origin: "コスタリカ",
    farm: "モンテジャノボニート農園",
    roastLevel: "浅煎り",
    processing: "レッドハニー",
    roaster: "+ninth coffee",
    roasterLink: "https://www.addninthcoffee.com/",
    tasteProfile: [
      { label: "酸味", value: 60 },
      { label: "苦味", value: 20 },
      { label: "甘み", value: 60 },
      { label: "コク", value: 80 },
      { label: "香り", value: 100 },
    ],
  },
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
    roaster: "LUSH-COFFEE",
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
  {
    id: "50464689684713",
    imageSrc: "/beanImages/50464689684713.jpg",
    tag: "中煎り",
    tagColor: "#A6683D",
    name: "ブルンジ",
    subName: "ブルンジ カランボ",
    description: "温かいうちに感じる赤りんごのような力強い甘さ。",
    detailDescription:
      "ブルンジ北東部ムインガの美しい丘陵地帯に位置するカランボWSは、約1,000人の小規模生産者が「ブルンジの最高品質のコーヒーを世に送り出す」というミッションのもとに集い、協力しながら歩調を合わせて品質向上に取り組んでいるウォッシングステーションです。\nその丁寧な栽培と精選体制から生まれるコーヒーは、クリーンで調和の取れた味わいが特長です。\nmarucacoffeeさんでは、2016年のオープン当初から継続してブルンジの豆を焙煎・提供されています。ブルンジは店主が特に愛着を持つ生産国のひとつであり、お店にとって欠かすことのできない存在です。",
    origin: "ブルンジ ムインガ",
    farm: "カランボ CWS",
    roastLevel: "中煎り",
    processing: "ウォッシュド",
    roaster: "marucacoffee",
    roasterLink: "https://marucacoffee.com//",
    tasteProfile: [
      { label: "酸味", value: 20 },
      { label: "苦味", value: 20 },
      { label: "甘み", value: 100 },
      { label: "コク", value: 60 },
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
];

export function getBeanById(id: string): BeanDetail | undefined {
  return beans.find((b) => b.id === id);
}
