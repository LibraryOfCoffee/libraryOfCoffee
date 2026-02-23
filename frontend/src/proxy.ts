import { type NextRequest, NextResponse } from "next/server";

export function proxy(request: NextRequest) {
  const basicAuth = process.env.BASIC_AUTH_CREDENTIALS;
  if (!basicAuth) return NextResponse.next();

  const authorization = request.headers.get("authorization");
  if (authorization) {
    const [, encoded] = authorization.split(" ");
    if (encoded && atob(encoded) === basicAuth) {
      return NextResponse.next();
    }
  }

  return new NextResponse("Unauthorized", {
    status: 401,
    headers: { "WWW-Authenticate": 'Basic realm="Protected"' },
  });
}

export const config = {
  matcher: ["/((?!_next/static|_next/image|favicon.ico).*)"],
};
