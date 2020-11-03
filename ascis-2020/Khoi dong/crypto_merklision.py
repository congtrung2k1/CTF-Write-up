from hashlib import sha256
from base64 import b64encode, b64decode

from typing import Tuple
ByteStrings = Tuple[bytes, ...]


def h(s: bytes) -> bytes:
    """Short notation for sha256. The digest is truncated to 6 bytes to save
    bandwidth."""
    return sha256(s).digest()[:6]


def merkle_hash(byte_strings: ByteStrings) -> bytes:
    """Get the root hash of a Merkle tree built from given byte strings. For
    more information, please visit https://en.wikipedia.org/wiki/Merkle_tree.
    """
    hashes = [h(s) for s in byte_strings]
    while len(hashes) > 1:
        if len(hashes) % 2 == 1:
            hashes.append(hashes[-1])
        hashes = [h(hashes[i] + hashes[i+1]) for i in range(0, len(hashes), 2)]
    return hashes[0]


def byte_strings_to_string(byte_strings: ByteStrings) -> str:
    """Represent a tuple of byte strings by a single string."""
    return b",".join([b64encode(s) for s in byte_strings]).decode()


def string_to_byte_strings(string: str) -> ByteStrings:
    """Parse a tuple of byte strings from a single string."""
    return tuple(b64decode(s) for s in string.split(','))


if __name__ == "__main__":
    # tell us your favourite number
    n = int(input())
    assert 0 <= n <= 2020

    # here's a hash collision finding challenge for you
    from string import ascii_letters
    from random import choices

    data = tuple(s.encode() for s in choices(ascii_letters, k=n))
    target = merkle_hash(data)
    print(byte_strings_to_string(data))

    # provide your collisions here
    seen_collisions = set()
    for i in range(2020):
        c = string_to_byte_strings(input())
        assert c not in seen_collisions
        seen_collisions.add(c)
        assert merkle_hash(c) == target

    # congrats :)
    from secret import flag
    print(flag)
