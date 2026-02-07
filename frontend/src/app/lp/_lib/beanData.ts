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
    description: "レモンやみかんの様な酸味とヘーゼルナッツを思わせる...",
    detailDescription: "XXXXXXXX",
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
    description: "レモンやみかんの様な酸味とヘーゼルナッツを思わせる...",
    detailDescription: "XXXXXXXX",
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
    description: "レモンやみかんの様な酸味とヘーゼルナッツを思わせる...",
    detailDescription: "XXXXXXXX",
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
    description: "レモンやみかんの様な酸味とヘーゼルナッツを思わせる...",
    detailDescription: "XXXXXXXX",
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
    description: "レモンやみかんの様な酸味とヘーゼルナッツを思わせる...",
    detailDescription: "XXXXXXXX",
    origin: "ジンバブエ・マニカランド州",
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
    description: "XXXXXXXX",
    detailDescription: "XXXXXXXX",
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
    description: "XXXXXXXX",
    detailDescription: "XXXXXXXX",
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
    tag: "中深煎り",
    tagColor: "#5C3317",
    name: "インドネシア",
    description: "XXXXXXXX",
    detailDescription: "XXXXXXXX",
    origin: "インドネシア スマトラ島北部",
    farm: "ビンタンリマの小農家さん",
    roastLevel: "中深煎り",
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
