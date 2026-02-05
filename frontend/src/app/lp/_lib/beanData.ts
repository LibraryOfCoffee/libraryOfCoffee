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
  roastLevel: string;
  processing: string;
  roaster: string;
  tasteProfile: TasteProfile[];
}

export const beans: BeanDetail[] = [
  {
    id: "49928992751849",
    imageSrc: "/beanImages/49928992751849.jpg",
    tag: "人気No.1",
    tagColor: "#8B5A2B",
    name: "東ティモール（中煎り）",
    description: "XXXXXXXXXXX",
    detailDescription:
      "XXXXXXXX",
    origin: "東ティモール, エルメラ県, レテフォホ郡",
    roastLevel: "中煎り",
    processing: "Fully Washed",
    roaster: "LUSH COFFEE",
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
    tag: "XX",
    tagColor: "#7D9B76",
    name: "東ティモール（中深煎り）",
    description: "XXXXXXXX",
    detailDescription:
      "XXXXXXXX",
    origin: "東ティモール, エルメラ県, レテフォホ郡",
    roastLevel: "中深煎り",
    processing: "Fully Washed",
    roaster: "LUSH COFFEE",
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
    tag: "おすすめ",
    tagColor: "#D4A574",
    name: "グアテマラ（中深煎り）",
    description: "XXXXXXXX",
    detailDescription:
      "XXXXXXXX",
    origin: "ウエウエテナンゴ",
    roastLevel: "中深煎り",
    processing: "Washed",
    roaster: "NORTH NODE COFFEE",
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
    tag: "限定",
    tagColor: "#6B5B4D",
    name: "インディア（深煎り）",
    description: "XXXXXXXX",
    detailDescription:
      "XXXXXXXX",
    origin: "シェバロイ地区",
    roastLevel: "深煎り",
    processing: "Washed",
    roaster: "NORTH NODE COFFEE",
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
    tag: "定番",
    tagColor: "#8B5A2B",
    name: "ルワンダ（中深煎り）",
    description: "XXXXXXXX",
    detailDescription:
      "XXXXXXXX",
    origin: "ジンバブエ・マニカランド州",
    roastLevel: "中煎り",
    processing: "Washed",
    roaster: "Tama Coffee Roaster",
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
    tag: "定番",
    tagColor: "#D4A574",
    name: "ホンジュラス（中深煎り）",
    description: "XXXXXXXX",
    detailDescription:
      "XXXXXXXX",
    origin: "インディブカ県マサグアラ",
    roastLevel: "中深煎り",
    processing: "Washed",
    roaster: "Tama Coffee Roaster",
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
    tag: "季節限定",
    tagColor: "#7D9B76",
    name: "コロンビア（浅煎り）",
    description: "XXXXXXXX",
    detailDescription:
      "XXXXXXXX",
    origin: "コロンビア",
    roastLevel: "浅煎り",
    processing: "サーマルショックナチュラル",
    roaster: "MOSHIMOSHI COFFEE",
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
    imageSrc: "/beanImages/49929003139305.png",
    tag: "人気",
    tagColor: "#8B5A2B",
    name: "インドネシア（中深煎り）",
    description: "XXXXXXXX",
    detailDescription:
      "XXXXXXXX",
    origin: "インドネシア スマトラ島北部 リントン・ニ・フタ 及び パランギナン",
    roastLevel: "中深煎り",
    processing: "スマトラ式",
    roaster: "MOSHIMOSHI COFFEE",
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
