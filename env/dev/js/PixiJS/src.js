import * as PIXI from "pixi.js";
import * as spine from "pixi-spine";
import * as sound from "pixi-sound";
import * as filters from "pixi-filters";

Object.assign(PIXI.filters, filters);
PIXI.sound = sound.default;

export {PIXI};