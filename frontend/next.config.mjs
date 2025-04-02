/** @type {import('next').NextConfig} */
const nextConfig = {
    images: {
        remotePatterns: [
          {
                protocol: 'https',
                hostname: 'stock.adobe.com',
                port: '',
                pathname: '/search',
                search: 'k=placeholder',
          },
          {
            protocol: 'https',
            hostname: 'plus.unsplash.com',
            port: '',
            pathname: '/**',
          },
        ],
      },
};

export default nextConfig;
