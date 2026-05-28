import bcrypt

hash_val = b"$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"
pwd = b"Admin1234"

try:
    match = bcrypt.checkpw(pwd, hash_val)
    print("Match:", match)
except Exception as e:
    print("Error:", e)
