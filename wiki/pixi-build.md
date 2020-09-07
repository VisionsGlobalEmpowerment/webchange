# Assembly PIXI.js build

To ensure compatibility, the pixi-related libraries should be compiled into a single file. For this, webpack is used. 
Below is a list of the building project files for the pixi assembly.

Main `index.js` file:

```js
import * as PIXI from "pixi.js";
import * as spine from "pixi-spine";
import * as filters from "pixi-filters";

Object.assign(PIXI.filters, filters);

export {PIXI};
```

Webpack config:

```js
var path = require('path');
var webpack = require('webpack');

module.exports = {
    mode: 'production',

    entry: './index.js',
    output: {
        filename: 'build.js',
        path: path.resolve(__dirname, 'dist'),
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
```

Package.json:

```json
{
  "name": "build-pixi",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {},
  "author": "",
  "license": "ISC",
  "dependencies": {
    "pixi-filters": "^3.1.1",
    "pixi-spine": "^2.1.9",
    "pixi.js": "^5.3.3",
    "babel-core": "^6.26.0",
    "babel-loader": "^7.1.4",
    "babel-preset-env": "^1.6.1"
  },
  "devDependencies": {
    "lodash": "^4.17.20",
    "webpack": "^4.44.1",
    "webpack-cli": "^3.3.12"
  }
}
```