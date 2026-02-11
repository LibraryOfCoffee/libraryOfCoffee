export const LOGIN_URL = "https://zcgqx8-tr.myshopify.com/";

export function moveToLoginPage() {
  window.location.href = "https://zcgqx8-tr.myshopify.com/";
}

export function moveToCoffeeBeanListPage(planId?: string, beanIds?: string[]) {
  const url = new URL("https://zcgqx8-tr.myshopify.com/pages/from-lp-to-login");
  if (planId) url.searchParams.set("planId", planId);
  if (beanIds) {
    beanIds.forEach((id, index) => {
      url.searchParams.set(`beanId${index + 1}`, id);
    });
  }
  window.location.href = url.toString();
}

export function getPlanPagePath(beanId?: string) {
  const params = beanId ? `?beanId=${beanId}` : "";
  return `/lp/plan${params}`;
}
