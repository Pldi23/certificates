const path = require("path");

const HtmlWebpackPlugin = require("html-webpack-plugin");

module.exports = {
    entry: "./src/index.js",
    output: {
        publicPath: '/',
        filename: 'bundle.js',
        path: path.join(__dirname, "/dist"),
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: ["babel-loader"]
            },
            {
                test: /\.css$/,
                use: ["style-loader", "css-loader"]
            },
            {
                test: /\.(png|svg|jpg|gif)$/,
                use: ['file-loader',],
            }
        ]
    },
    devtool: 'source-map',
    devServer: {
        historyApiFallback: true,
        contentBase: path.join(__dirname, "public/"),
        port: 3000,
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: "./src/index.html",

        })
    ]
};