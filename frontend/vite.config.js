import {defineConfig} from 'vite';
import react from '@vitejs/plugin-react';
import babel from '@rollup/plugin-babel';

export default defineConfig({
    server: {
        port: 3000,
    },
    esbuild: {
        loader: 'jsx',
        include: [
            // Add this for business-as-usual behaviour for .jsx and .tsx files
            'src/**/*.jsx',
            'src/**/*.tsx',
            'node_modules/**/*.jsx',
            'node_modules/**/*.tsx',

            // Add the specific files you want to allow JSX syntax in
            'src/LocalJsxInJsComponent.js',
            'node_modules/bad-jsx-in-js-component/index.js',
            'node_modules/bad-jsx-in-js-component/js/BadJSXinJS.js',
            'node_modules/bad-jsx-in-js-component/ts/index.ts',
            'node_modules/bad-jsx-in-js-component/ts/BadTSXinTS.ts',

            // --- OR ---

            // Add these lines to allow all .js files to contain JSX
            'src/**/*.js',
            'node_modules/**/*.js',

            // Add these lines to allow all .ts files to contain JSX
            'src/**/*.ts',
            'node_modules/**/*.ts',
        ],
    },
    optimizeDeps: {
        esbuildOptions: {
            loader: {
                '.js': 'jsx',
            },
        },
    },
    build: {
        outDir: '../src/main/resources/static',
        sourcemap: true
    },
    plugins: [
        babel({
            exclude: 'node_modules/**',
            presets: ['@babel/preset-react'],
            babelHelpers: 'bundled',
        }),
        react(),
    ],
});
