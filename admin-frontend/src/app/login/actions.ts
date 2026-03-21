"use server";

import { cookies } from "next/headers";
import { redirect } from "next/navigation";
import { z } from "zod/v4";
import { createAdminApiClient } from "@/api/client";

const loginSchema = z.object({
  email: z.email("有効なメールアドレスを入力してください。"),
  password: z.string().min(1, "パスワードを入力してください。"),
});

export type LoginState = {
  error?: string;
  fieldErrors?: {
    email?: string[];
    password?: string[];
  };
};

export async function loginAction(
  _prevState: LoginState,
  formData: FormData,
): Promise<LoginState> {
  const result = loginSchema.safeParse({
    email: formData.get("email"),
    password: formData.get("password"),
  });

  if (!result.success) {
    return { fieldErrors: result.error.flatten().fieldErrors };
  }

  const { email, password } = result.data;

  const client = createAdminApiClient();
  const { data, error } = await client.POST("/api/admin/auth/login", {
    body: { email, password },
  });

  if (error || !data?.accessToken) {
    return { error: "メールアドレスまたはパスワードが正しくありません。" };
  }

  const cookieStore = await cookies();
  cookieStore.set("accessToken", data.accessToken, {
    httpOnly: true,
    secure: process.env.NODE_ENV === "production",
    sameSite: "lax",
    path: "/",
    maxAge: data.expiresIn ?? 3600,
  });

  redirect("/");
}
