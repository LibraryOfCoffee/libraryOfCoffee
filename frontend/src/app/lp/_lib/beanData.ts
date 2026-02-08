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
    description: "レモンやみかんの様な酸味とヘーゼルナッツを思わせる",
    detailDescription:
      "19世紀に当時の東ティモールを統治していたポルトガルが、高地の気候を活かして栽培を始めたのが起源です。現在は小規模農家が中心となって生産され、熟した果実のようなやさしい甘さが特徴。世界的なスペシャルティコーヒー人気を背景に、丁寧な精製と品質重視の豆として注目されています。\n\n【LUSH COFFEEの東ティモール（中煎り）】\n・レモンやみかんの様な酸味とヘーゼルナッツを思わせる風味。\n・サトウキビらしい砂糖の穏やかな甘味も感じさせ、デイリーのコーヒーに適したテイスト。",
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
    description: "キャラメル、プラムの甘さ、くるみの香ばしさ",
    detailDescription:
      "・キャラメル、プラムの甘さ、くるみの香ばしさ\n・グレープフルーツのようなハーブと柑橘系の風味を楽しめるコーヒーです\n\n【東ティモールってどんなコーヒー？】\n19世紀に当時の東ティモールを統治していたポルトガルが、高地の気候を活かして栽培を始めたのが起源です。現在は小規模農家が中心となって生産され、熟した果実のようなやさしい甘さが特徴。世界的なスペシャルティコーヒー人気を背景に、丁寧な精製と品質重視の豆として注目されています。",
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
    description: "ハイカカオのチョコレートのような酸と、ほどよい苦味",
    detailDescription:
      "ハイカカオのチョコレートのような酸と、ほどよい苦味が特徴です。\n\n【グァテマラ・コンポステラってどんなコーヒー？】\nグアテマラは中米に位置し、栄養豊富な火山性土壌と高地の気候に恵まれたコーヒー産地です。標高の高いエリアでは寒暖差が大きく、実がゆっくり成熟することで豆が引き締まり、風味の良いコーヒーが育ちやすいのが特徴。その中でも標高1,500m以上の高地で育つ豆は、SHB（Strictly Hard Bean）と呼ばれる最上位クラスに分類されます。こうした貴重なSHB豆の中から厳選されたロットに、「コンポステラ（星の野原）」という特別な名前が付けられました。",
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
    description: "ナッツ系の香ばしさとペッパーのようなキレのある爽やかさ",
    detailDescription:
      "ナッツ系の香ばしさの中にペッパーのようなキレのある爽やかさが特徴です。\n\n【インディアAPAAってどんなコーヒー？】\nインドは南部を中心に高原地帯が広がり、穏やかな気候の中でじっくり成熟するため、角の取れた飲み口になりやすいのが魅力。APAAは「Arabica Plantation AA」の略で、サイズや形が基準を満たし、異物混入のない高品質なアラビカ種（washed製法）であることを保証。インドコーヒーの特徴である独特なスパイスのような香りが特徴です。",
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
    description: "バレンシアオレンジや八朔を思わせるフレッシュで香り高い柑橘の酸と三温糖の上品な甘さ",
    detailDescription:
      "バレンシアオレンジや八朔を思わせるフレッシュで香り高い柑橘の酸の印象と三温糖の上品な甘さが特徴です。\n\n【ルワンダ（シーラCWS）ってどんなコーヒー？】\nルワンダは東アフリカ内陸の高原国で、「千の丘の国」と呼ばれるほど起伏に富んだ地形が特徴。標高1,500〜2,000mの高地で栽培されるコーヒーは、昼夜の寒暖差によってゆっくり成熟し、複雑な風味が生まれます。シーラCWS（Coffee Washing Station）は品質管理が徹底された精製施設で、フルーティーでクリーンな味わいのコーヒーを生産しています。",
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
    description: "シルクのような飲み口に、オレンジやプラムなど華やかな風味と心地よい甘さ",
    detailDescription:
      "シルクのような飲み口に、オレンジやプラムなど華やかな風味。滑らかな質感と心地よい甘さが特徴です。\n\n【ホンジュラスってどんなコーヒー？】\nホンジュラスは中米の山岳国で、18〜19世紀にコーヒー栽培が始まり、20世紀に入って輸出産業として本格化しました。近年は品質改善と市場開拓が進み、中米最大級のコーヒー輸出国として成長。国際品評会Cup of Excellence（COE）でも入賞ロットが高く評価され、スペシャルティ産地としての認知が高まっています。\n\n今回の豆は、高地（標高約1,800m）で育つティピカ種です。この地域は小規模農家が中心で、冷涼な環境の中でチェリーがゆっくり成熟するため、クリーンでバランスの良い風味が生まれやすいのが特徴です。",
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
    description: "ブルーベリーの爆発的なフレーバーとヨーグルトのような酸味、チョコレートの余韻",
    detailDescription:
      "・ブルーベリーの爆発的なフレーバー、後からヨーグルトのような酸味。\n・最後にはチョコレートで締めくくってくれる豆です。\n\n【コロンビアってどんなコーヒー？】\nコロンビアは南米アンデス山脈沿いに広がる世界有数のコーヒー生産国で、19世紀後半から主要産業として発展してきました。標高差のある山岳地帯と一年を通して安定した気候により、地域ごとに異なる風味が生まれるのが特徴です。\n\n今回の豆は、発酵の途中で温度差を与える「サーマルショックナチュラル」という精製方法を採用。果実の香りを豆の中に閉じ込めることで、フルーティーな風味をはっきりと引き出しています。\n\n品種はスーダンルメという珍しい品種。アフリカ由来の希少品種で、病害に強く、果実感のある華やかな香りと透明感のある甘さが出やすいことで知られています。近年コロンビアでは、高品質かつ安定生産できる品種として栽培が広がっており、浅煎りにすることで、こうしたスーダンルメ本来の個性を引き立てます。",
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
    description: "オレンジピールやグレープフルーツの明るい風味からカカオのほろ苦さまで",
    detailDescription:
      "・オレンジピールやグレープフルーツの明るい風味\n・果物のフレーバーから徐々にカカオのほろ苦さまで楽しめる珈琲です！\n\n【インドネシア/マンデリン/ビンタンリマってどんなコーヒー？】\nインドネシアは赤道直下に広がる島国で、18世紀からコーヒー栽培が続く世界有数の生産国。中でもスマトラ島北部は、火山性土壌と多雨な高地環境により、コクのある個性的なコーヒーが育つ産地。\n\nここを代表するマンデリンは、重厚なコクと丸みのある口当たりが特徴で、現地特有の「スマトラ式」精製により、土やハーブを思わせる深みのある風味が生まれます。\n\nまたビンタンリマはインドネシア語で「五つ星」を意味する品質呼称で、特定の農園名ではなく、複数の小規模農家の中から完熟チェリーのみを厳選し、欠点豆の少なさやカップ品質を満たしたロットに付けられる名称。マンデリンの中でも安定したクオリティが評価され、日本でも定番として親しまれています。",
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
