export function parseTastesFromFormData(
  formData: FormData,
): { tasteId: string; evaluationValue: number }[] {
  const tasteIds = formData.getAll("tasteIds") as string[];
  return tasteIds
    .map((tasteId) => {
      const rawValue = formData.get(`tasteValue_${tasteId}`) as string;
      const evaluationValue = Number.parseInt(rawValue, 10);
      if (Number.isNaN(evaluationValue)) return null;
      return { tasteId, evaluationValue };
    })
    .filter(
      (t): t is { tasteId: string; evaluationValue: number } => t !== null,
    );
}
