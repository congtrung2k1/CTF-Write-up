restrictions = [
 'uudcjkllpuqngqwbujnbhobowpx_kdkp_',
 'f_negcqevyxmauuhthijbwhpjbvalnhnm',
 'dsafqqwxaqtstghrfbxzp_x_xo_kzqxck',
 'mdmqs_tfxbwisprcjutkrsogarmijtcls',
 'kvpsbdddqcyuzrgdomvnmlaymnlbegnur',
 'oykgmfa_cmroybxsgwktlzfitgagwxawu',
 'ewxbxogihhmknjcpbymdxqljvsspnvzfv',
 'izjwevjzooutelioqrbggatwkqfcuzwin',
 'xtbifb_vzsilvyjmyqsxdkrrqwyyiu_vb',
 'watartiplxa_ktzn_ouwzndcrfutffyzd',
 'rqzhdgfhdnbpmomakleqfpmxetpwpobgj',
 'qggdzxprwisr_vkkipgftuvhsizlc_pbz',
 'jerzhlnsegcaqzathfpuufwunakdtceqw',
 'lbvlyyrugffgrwo_v_zrqvqszchqrrljq',
 'aiwuuhzbszvfpidwwkl_wynlujbsbhfox',
 'vmhrizxtiegxdxsqcdoiyxkffloudwtxg',
 'tffjnabob_jbf_qiszdsemczghnjysmah',
 'zrqkppvynlkelnevngwlkhgaputhoagtt',
 'nl_oojyafwoqccbedijmigpedkdzglq_f',
 'cksy_skctjlyxktuzchvstunyvcvabomc',
 'ppcxleeguvhvhengmvac_bykhzqohjuei',
 '_clmaicjrrzhwd_fescyaejtbyefxyihy',
 'hhopvwsmjtpjiffzatyhjrev_dwnsidyo',
 'sjevtrmkkk_zjalxrxfovjsbcxjx_pskp',
 'gnynwuuqypddbsylparpcczqimimqmvdl',
 'bxitcmhnmanwuhvjxnqeoiimlegrmkjra']

f_len = len(restrictions[0])

a = [chr(i) for i in range(95, 95 + 28)]
a = a[0:1] + a[2:]
flag = ""

for i in range(f_len):
	ch = ""
	for item in restrictions:
		ch += item[i]
	for j in a:
		if j not in ch:
			flag += j
			break

capital = [0, 4, 9, 19, 23, 26]
cap_flag = ''
for f in range(len(flag)):
    if f in capital:
        cap_flag += flag[f].upper()
    else:
        cap_flag += flag[f]
print(cap_flag)