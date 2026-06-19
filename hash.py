import bcrypt

hash_val = b"$2a$10$9l8I0KkUSJlNL2/HOLREHefUbLb1HC540oRnuEM36NJzZougRFP.i"
pwd = b"Admin1234"

try:
    match = bcrypt.checkpw(pwd, hash_val)
    print("Match:", match)
except Exception as e:
    print("Error:", e)
