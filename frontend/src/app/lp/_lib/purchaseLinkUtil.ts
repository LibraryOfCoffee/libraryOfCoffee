export const LOGIN_URL = "https://zcgqx8-tr.myshopify.com/";
export const COFFEE_BEAN_LIST_URL =
  "https://zcgqx8-tr.myshopify.com/pages/cbl_enter";

export function moveToLoginPage() {
  window.location.href = "https://zcgqx8-tr.myshopify.com/";
}

export function moveToCoffeeBeanListPage() {
  window.location.href = "https://zcgqx8-tr.myshopify.com/pages/cbl_enter";
}

export function getPlanPagePath(beanId?: string) {
  const params = beanId ? `?bean=${beanId}` : "";
  return `/lp/plan${params}`;
}
