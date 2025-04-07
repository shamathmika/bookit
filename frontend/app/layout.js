
import { Header } from "@/components/common/Header";
import "./globals.css";


export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body
        className={` antialiased`}
        suppressHydrationWarning
      >

        {children}
      </body>
    </html>
  );
}
