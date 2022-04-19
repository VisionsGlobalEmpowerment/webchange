# `:metadata`

## Common

- `:display-name` - used to show user-friendly object name
- `:removable?` - set `true` if text object can be deleted. `false` by default
- `:objects-tree` - settings for 'Scene Objects' control in right side menu
  - `:show?` - set `true` to show object in list
  - `:actions` - use to specify available actions for current object
  - `:sort-order` - integer number used sorting objects in objects list

## Text object

- `:text-animation-target?` - set `false` to hide from list of available [text-animation](../actions/text-animation.md) targets
- `:page-idx` - index of flipbook page where text object is placed. Used for available text animation targets sorting
- `:text-idx` index of text object inside flipbook page. Used for available text animation targets sorting
