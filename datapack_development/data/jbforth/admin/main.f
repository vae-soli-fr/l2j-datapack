\ GM commands handlers

admin/help
admin/teleports
admin/polymorph
admin/ride

: bypass_admin_menu
	"admin-menu" check-access
	'<button value="Play Sounds" action="bypass -h admin_play_sounds" width=90 height=15 back="sek.cbui94" fore="sek.cbui92">'
	'<button value="Paralyze" action="bypass -h forth player@ target@ paralyze" width=90 height=15 back="sek.cbui94" fore="sek.cbui92">' S+
	'<button value="Unparalyze" action="bypass -h forth player@ target@ unparalyze" width=90 height=15 back="sek.cbui94" fore="sek.cbui92">' S+
	show
;

: gm_set-hp
	\ Set target HP

	"char-modify" check-access
	
	float
	player@ target@
	"CurrentHp" set(d)
;

: gm_set-mp
	\ Set target HP

	"char-modify" check-access
	
	float
	player@ target@
	"CurrentMp" set(d)
;
