var path = require('path');

module.exports = {
    mode: 'production',
    entry: './src.js',
    output: {
        filename: 'audio-script.js',
        path: path.resolve(__dirname, '../../../../src/libs/'),
        libraryTarget: 'commonjs'
    },
    module: {
        rules: [{
            test: /\.js$/,
            loader: "babel-loader",
            exclude: /(node_modules)/
        }]
    }
};
