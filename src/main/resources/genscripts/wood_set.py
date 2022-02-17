import sys, os, json, re
from os import system as run

# TODO allow input from cmd
modid = 'quark'
category = 'world'
flag = 'azalea_wood'

def main():
	for arg in sys.argv:
		if not '.py' in arg:
			if '=' in arg:
				pass # TODO
			else:
				makeWood(arg)

def makeWood(type):
	#run(f"py generic_block.py {type}_planks")
	#run(f"py pillar.py {type}_log stripped_{type}_log")
	#run(f"py wood_block.py {type} stripped_{type}")
	#run(f"py stairs_slabs.py category={category} flag={flag} {type}_planks")
	#run(f"py post_modded.py flag={flag} {type} stripped_{type}")
	#run(f"py bookshelves.py {type}")
	#run(f"py chest.py {type}")
	#run(f"py ladder.py {type}")
	#run(f"py door.py {type}")
	#run(f"py trapdoor.py {type}")
	#run(f"py fence_gates.py {type}")
	#run(f"py fences.py {type}")
	#run(f"py sign.py {type}")
	#run(f"py buttons.py texname={type}_planks {type}")
	#run(f"py pressure_plates.py texname={type}_planks {type}")
	#run(f"py generic_item.py {type}_boat")

	appendTags(type)

def appendTags(type):
	print('')
	print('Appending tags for', type)

	bulkTag(['logs', 'logs_that_burn', f"{modid}:{type}_logs"], type, ["%_log", "stripped_%_log", "%_wood", "stripped_%_wood"])
	addToTag('quark:ladders', type, ["%_ladder"])
	addToTag('climbable', type, ["%_ladder"], False)
	addToTag('planks', type, ["%_plank"])
	addToTag('wooden_stairs', type, ["%_plank_stairs"])
	addToTag('wooden_slabs', type, ["%_plank_slab"])
	addToTag('quark:wooden_vertical_slabs', type, ["%_plank_vertical_slab"])
	bulkTag(['trapdoors', 'wooden_trapdoors'], type, ["%_trapdoor"])
	bulkTag(['fences', 'wooden_fences', 'forge:fences', 'forge:fences/wooden'], type, ["%_fence"])
	bulkTag(['fence_gates', 'forge:fence_gates', 'forge:fence_gates/wooden'], type, ["%_fence_gate"])
	bulkTag(['buttons', 'wooden_buttons'], type, ["%_button"])
	bulkTag(['pressure_plates', 'pressure_plates'], type, ["%_pressure_plate"])
	bulkTag(['doors', 'wooden_doors'], type, ["%_door"])
	addToTag('forge:bookshelves', type, ["%_bookshelf"])
	bulkTag(['signs', 'standing_signs'], type, ["%_sign"], False)
	bulkTag(['signs', 'wall_signs'], type, ["%_wall_sign"], False)
	bulkTag(['forge:chests', 'forge:chests/wooden', 'guarded_by_piglins'], type, ["%_chest", "%_trapped_chest"])
	addToTag('forge:chests/trapped', type, ["%_trapped_chest"])

def bulkTag(tags, type, items, mirror=True, is_block=True):
	for tag in tags:
		addToTag(tag, type, items, mirror, is_block)

def addToTag(tag, type, items, mirror=True, is_block=True):
	if mirror:
		addToTag(tag, type, items, False, not is_block)

	if ':' in tag:
		resloc = tag.split(':')
	else:
		resloc = ['minecraft', tag]

	tag_type = 'blocks' if is_block else 'items'
	path = f"../data/{resloc[0]}/tags/{tag_type}/{resloc[1]}.json"

	if not os.path.exists(path):
		parent = re.sub(r'/[^/]+$', '', path)
		os.makedirs(parent, exist_ok=True)

		with open(path, 'w') as f:
			f.write('{ "replace": false, "values": [] }')

	with open(path, 'r') as f:
		data = json.load(f)
		values = data['values']

		changed = False
		for raw_item in items:
			item = f"{modid}:{raw_item}"
			item = item.replace('%', type)

			if not item in values:
				values.append(item)
				changed = True
		
		if changed:
			with open(path, 'w') as fw:
				json.dump(data, fw, indent=4)
				print('Updating', path)

main()