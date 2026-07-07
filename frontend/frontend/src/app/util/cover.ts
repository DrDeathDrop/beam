/** Deterministic cover-art helpers so every game gets a stable, distinct look
 *  without needing real artwork from the backend. */

export function coverGradient(seed: string): string {
  let h = 0;
  for (let i = 0; i < seed.length; i++) {
    h = (h * 31 + seed.charCodeAt(i)) >>> 0;
  }
  const a = h % 360;
  const b = (a + 45 + ((h >> 8) % 90)) % 360;
  return `linear-gradient(135deg, hsl(${a} 52% 34%), hsl(${b} 58% 18%))`;
}

export function coverInitials(title: string): string {
  return title
    .split(/\s+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((w) => w[0])
    .join('')
    .toUpperCase();
}
