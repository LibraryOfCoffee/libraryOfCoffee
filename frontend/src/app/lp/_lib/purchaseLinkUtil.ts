export const LOGIN_URL = "https://zcgqx8-tr.myshopify.com/";
export const COFFEE_BEAN_LIST_URL =
  "https://zcgqx8-tr.myshopify.com/pages/cbl_enter";

export function moveToLoginPage() {
  window.location.href = "https://zcgqx8-tr.myshopify.com/";
}

export function moveToCoffeeBeanListPage(planId?: string, beanIds?: string[]) {
  const url = new URL("https://zcgqx8-tr.myshopify.com/pages/cbl_enter");
  if (planId) url.searchParams.set("planId", planId);
  if (beanIds) {
    for (const id of beanIds) {
      url.searchParams.append("beanId", id);
    }
  }
  window.location.href = url.toString();
}

export function getPlanPagePath(beanId?: string) {
  const params = beanId ? `?beanId=${beanId}` : "";
  return `/lp/plan${params}`;
}
