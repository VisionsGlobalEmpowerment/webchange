var path = require('path');
var webpack = require('webpack');

module.exports = {
    mode: 'development',
    entry: './src.js',
    output: {
        filename: 'pixi-build.js',
        path: path.resolve(__dirname, '../../../../src/libs/'),
        libraryTarget: 'commonjs'
    },
    module: {
        rules: [{
            test: /\.js$/,
            loader: "babel-loader",
            exclude: /(node_modules)/
        }]
    },
    plugins: [
        new webpack.ProvidePlugin({
            "PIXI": "pixi.js"
        })
    ]
};
